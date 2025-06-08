// scripts.js

// Replace with your endpoint
const API_KEY_URL = "http://127.0.0.1:1212/api/api_key";
let API_KEY = "";

// YouTube IFrame API setup
const players = {};
const queue = [];

function onYouTubeIframeAPIReady() {
  queue.forEach(cfg => _initPlayer(cfg));
  queue.length = 0;
}

function _initPlayer({ elementId, videoId }) {
  players[videoId] = new YT.Player(elementId, {
    height: '180',
    width: '320',
    videoId,
    playerVars: {
      controls: 0,
      modestbranding: 1,
      rel: 0,
      iv_load_policy: 3
    },
    events: {
      onReady: event => onPlayerReady(event, videoId),
      onStateChange: onPlayerStateChange
    }
  });
}

function onPlayerReady(event, videoId) {
  const player = event.target;
  const container = document.getElementById(`container-${videoId}`);
  const seekBar = container.querySelector('.seek-bar');

  // hide iframe until play
  const iframe = container.querySelector('iframe');
  if (iframe) iframe.style.display = 'none';

  // once we know duration, set max on seek bar
  const duration = player.getDuration();
  seekBar.max = Math.floor(duration);

  // update slider as video plays
  setInterval(() => {
    if (player.getCurrentTime) {
      seekBar.value = Math.floor(player.getCurrentTime());
    }
  }, 500);

  // scrub when user drags
  seekBar.addEventListener('input', e => {
    player.seekTo(Number(e.target.value), true);
  });
}

function onPlayerStateChange(event) {
  const id = event.target.getVideoData().video_id;
  const container = document.getElementById(`container-${id}`);
  const iframe = container.querySelector('iframe');
  // show iframe when playing, hide when paused/ended
  if (event.data === YT.PlayerState.PLAYING) {
    iframe.style.display = 'block';
  } else if (event.data === YT.PlayerState.PAUSED || event.data === YT.PlayerState.ENDED) {
    iframe.style.display = 'none';
  }
}

// Fetch API key and initialize albums
window.onload = async () => {
  try {
    const res = await fetch(API_KEY_URL);
    API_KEY = (await res.text()).trim();
    buildAlbums();
  } catch (e) {
    console.error('API key error', e);
  }
};

// Album definitions
const albums = [
  { title: 'Top Hits', query: 'Top Hits 2025' },
  { title: 'Chill Vibes', query: 'Chill music playlist' },
  { title: 'Workout Mix', query: 'Workout music mix' }
];

function buildAlbums() {
  const albumsDiv = document.getElementById('albums');
  albums.forEach(album => {
    const box = document.createElement('div');
    box.className = 'album';
    box.innerHTML = `<h2>${album.title}</h2>
                     <div class="videos" id="album-${album.title.replace(/\s+/g, '-')}"></div>`;
    albumsDiv.appendChild(box);
    fetchAlbumVideos(album, box.querySelector('.videos'));
  });
}

async function fetchAlbumVideos({ query }, container) {
  const url = new URL('https://www.googleapis.com/youtube/v3/search');
  url.searchParams.set('part', 'snippet');
  url.searchParams.set('type', 'video');
  url.searchParams.set('maxResults', 5);
  url.searchParams.set('videoEmbeddable', 'true');
  url.searchParams.set('q', query);
  url.searchParams.set('key', API_KEY);

  try {
    const res = await fetch(url);
    const data = await res.json();
    (data.items || []).forEach(item => insertVideo(item, container));
  } catch (err) {
    console.error('Fetch error:', err);
  }
}

function insertVideo(item, container) {
  const vid = item.id.videoId;
  const title = item.snippet.title;

  const wrapper = document.createElement('div');
  wrapper.id = `container-${vid}`;
  wrapper.className = 'video-container';

  wrapper.innerHTML = `
    <p><strong>${title}</strong></p>
    <div id="player-${vid}" class="player-placeholder"></div>
    <div class="controls">
      <button data-action="play" data-vid="${vid}">▶️ Play</button>
      <button data-action="pause" data-vid="${vid}">⏸ Pause</button>
      <button data-action="mute" data-vid="${vid}">🔇 Mute</button><br/>
      <input type="range" class="seek-bar" data-vid="${vid}" value="0" min="0" max="0" />
    </div>
  `;
  container.appendChild(wrapper);

  const cfg = { elementId: `player-${vid}`, videoId: vid };
  if (window.YT && YT.Player) _initPlayer(cfg);
  else queue.push(cfg);

  // wire controls
  wrapper.querySelector('.controls').addEventListener('click', e => {
    const action = e.target.dataset.action;
    const id = e.target.dataset.vid;
    const player = players[id];
    if (!player) return;
    if (action === 'play') player.playVideo();
    if (action === 'pause') player.pauseVideo();
    if (action === 'mute') player.isMuted() ? player.unMute() : player.mute();
  });
}
