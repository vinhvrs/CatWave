
// Helper to format seconds into M:SS
function formatTime(sec) {
  const m = Math.floor(sec / 60);
  const s = sec % 60;
  return `${m}:${s.toString().padStart(2, '0')}`;
}

// Endpoint to fetch your API key
const API_KEY_URL = "http://127.0.0.1:1212/api/api_key";
let API_KEY = "";
let uid = localStorage.getItem("uid");

// fetch playlists from Database
async function fetchPlaylist() {
    const res = await fetch(`api/${uid}/playlists`);
    if (!res.ok) {
        console.error("Failed to fetch playlists");
        return [];
    }
    return await res.json();
}

// Handler when user clicks a playlist button
function handlePlaylistClick(pid, pname) {
    const old = document.getElementById('playlists');
    if (old) old.innerHTML = '';
    loadPlaylist(pid, pname);
}

// On page load: fetch user's playlists, render buttons, and load all playlists
window.addEventListener('DOMContentLoaded', async () => {
    // fetch API key
    try {
        const res = await fetch(API_KEY_URL);
        API_KEY = (await res.text()).trim();
    } catch (e) {
        console.error('API key error', e);
    }
    // Fetch and render playlist buttons
    const list = await fetchPlaylist();
    const userDiv = document.getElementById('userPlaylists');
    list.forEach(data => {
        const btn = document.createElement('button');
        btn.textContent = data.pname;
        btn.onclick = () => handlePlaylistClick(data.pid, data.pname);
        userDiv.appendChild(btn);
        // Automatically load each playlist with its title
        loadPlaylist(data.pid, data.pname);
    });
});

// YouTube IFrame API helpers
const players = {}, queue = [];
function onYouTubeIframeAPIReady() {
    queue.forEach(cfg => _initPlayer(cfg));
    queue.length = 0;
}
function _initPlayer({ elementId, videoId }) {
    players[videoId] = new YT.Player(elementId, {
        height: '180', width: '320', videoId,
        playerVars: { controls: 0, modestbranding: 1, rel: 0, iv_load_policy: 3 },
        events: { onReady: (e) => onPlayerReady(e, videoId), onStateChange: onPlayerStateChange }
    });
}

function onPlayerReady(event, videoId) {
    const player = event.target;
    const container = document.getElementById(`container-${videoId}`);
    const seekBar = container.querySelector('.seek-bar');
    const timeDisplay = container.querySelector('.time-display');
    const iframe = container.querySelector('iframe');
    if (iframe) iframe.style.display = 'none';

    const duration = Math.floor(player.getDuration());
    seekBar.max = duration;
    if (timeDisplay) timeDisplay.textContent = `0:00 / ${formatTime(duration)}`;

    setInterval(() => {
        const current = Math.floor(player.getCurrentTime());
        seekBar.value = current;
        if (timeDisplay) timeDisplay.textContent = `${formatTime(current)} / ${formatTime(duration)}`;
    }, 500);

    seekBar.addEventListener('input', e => {
        const val = Number(e.target.value);
        player.seekTo(val, true);
        if (timeDisplay) timeDisplay.textContent = `${formatTime(val)} / ${formatTime(duration)}`;
    });
}

function onPlayerStateChange(event) {
    const vid = event.target.getVideoData().video_id;
    const cont = document.getElementById(`container-${vid}`);
    const iframe = cont.querySelector('iframe');
    if (event.data === YT.PlayerState.PLAYING) iframe.style.display = 'block';
    else if ([YT.PlayerState.PAUSED, YT.PlayerState.ENDED].includes(event.data)) iframe.style.display = 'none';
}

// Load videos using songs from your backend rather than YouTube playlistItems
async function loadPlaylist(playlistId, playlistName) {
    // Fetch song IDs (sid) for this playlist
    const res = await fetch(`/api/playlists/${playlistId}/songs`);
    if (!res.ok) {
        console.error(`Failed to fetch songs for playlist ${playlistId}`);
        return;
    }
    const songs = await res.json();

    const container = document.getElementById('playlists');
    const box = document.createElement('div'); box.className = 'playlist';
    box.innerHTML = `<h2>${playlistName}</h2><div class="videos" id="list-${playlistId}"></div>`;
    container.appendChild(box);
    const videosDiv = document.getElementById(`list-${playlistId}`);

    songs.forEach(item => {
        insertVideo(item.sid, item.audioUrl || item.sid, `list-${playlistId}`);
    });
}

// Insert a single video into its list container
function insertVideo(vid, title, listId) {
    const container = document.getElementById(listId);
    const wrap = document.createElement('div');
    wrap.id = `container-${vid}`;
    wrap.className = 'video-container';
    wrap.innerHTML = `
        <p><strong>${title}</strong></p>
        <div id="player-${vid}" class="player-placeholder"></div>
        <div class="controls">
          <button data-action="play" data-vid="${vid}">▶️ Play</button>
          <button data-action="pause" data-vid="${vid}">⏸ Pause</button>
          <button data-action="mute" data-vid="${vid}">🔇 Mute</button><br/>
          <input type="range" class="seek-bar" data-vid="${vid}" value="0" min="0" max="0" />
          <span class="time-display">0:00 / 0:00</span>
        </div>
    `;
    container.appendChild(wrap);

    const cfg = { elementId: `player-${vid}`, videoId: vid };
    if (window.YT && YT.Player) _initPlayer(cfg);
    else queue.push(cfg);

    wrap.querySelector('.controls').addEventListener('click', e => {
        const action = e.target.dataset.action;
        const player = players[e.target.dataset.vid];
        if (!player) return;
        if (action === 'play') player.playVideo();
        if (action === 'pause') player.pauseVideo();
        if (action === 'mute') player.isMuted() ? player.unMute() : player.mute();
    });
}
