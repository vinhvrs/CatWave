var API_KEY = "";
const players = {}; // map videoId → YT.Player
const queue = [];

// YouTube IFrame API ready callback
function onYouTubeIframeAPIReady() {
  queue.forEach(cfg => _initPlayer(cfg));
  queue.length = 0;
}

// Helper to instantiate a YT.Player with custom events
function _initPlayer({ elementId, videoId }) {
  players[videoId] = new YT.Player(elementId, {
    height: '180',
    width: '320',
    videoId: videoId,
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
  const duration = player.getDuration();
  seekBar.max = Math.floor(duration);

  setInterval(() => {
    if (player && player.getCurrentTime) {
      seekBar.value = Math.floor(player.getCurrentTime());
    }
  }, 500);

  seekBar.addEventListener('input', e => {
    player.seekTo(Number(e.target.value), true);
  });
}

function onPlayerStateChange(event) {}

// Format view count
function formatViews(views) {
  views = Number(views);
  if (views >= 1000000) return (views / 1000000).toFixed(1) + "M";
  if (views >= 1000) return (views / 1000).toFixed(1) + "K";
  return views;
}

// Fetch API_KEY on page load
onload = async function () {
  try {
    const res = await fetch("http://127.0.0.1:1212/api/api_key");
    if (!res.ok) throw new Error("API key fetch failed");
    API_KEY = (await res.text()).trim();
  } catch (e) {
    console.error(e);
  }
};

document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('searchForm');
  const input = document.getElementById('searchInput');
  const resultsDiv = document.getElementById('results');

  let currentQuery = "";
  let nextPageToken = "";
  let totalLoaded = 0;
  const maxItems = 50;
  const pageSize = 10;
  let isLoading = false;

  form.addEventListener('submit', e => {
    e.preventDefault();
    resultsDiv.innerHTML = "";
    currentQuery = input.value.trim();
    nextPageToken = "";
    totalLoaded = 0;
    if (currentQuery) searchYouTube(currentQuery);
  });

  async function searchYouTube(query) {
    if (isLoading || totalLoaded >= maxItems) return;
    isLoading = true;

    try {
      const searchUrl = new URL("https://www.googleapis.com/youtube/v3/search");
      searchUrl.searchParams.set("part", "snippet");
      searchUrl.searchParams.set("type", "video");
      searchUrl.searchParams.set("maxResults", pageSize);
      searchUrl.searchParams.set("q", query);
      searchUrl.searchParams.set("key", API_KEY);
      if (nextPageToken) searchUrl.searchParams.set("pageToken", nextPageToken);

      const searchRes = await fetch(searchUrl);
      const searchData = await searchRes.json();
      const videoIds = searchData.items.map(item => item.id.videoId).join(',');

      if (!videoIds) return;

      const videosUrl = new URL("https://www.googleapis.com/youtube/v3/videos");
      videosUrl.searchParams.set("part", "snippet,statistics");
      videosUrl.searchParams.set("id", videoIds);
      videosUrl.searchParams.set("key", API_KEY);

      const videosRes = await fetch(videosUrl);
      const videosData = await videosRes.json();
      const items = videosData.items || [];

      if (items.length > 0) {
        insertVideosSmartly(items);
        storeSong(items);
        nextPageToken = searchData.nextPageToken || "";
      }
    } catch (err) {
      console.error("Fetch error:", err);
    } finally {
      isLoading = false;
    }
  }

  function insertVideosSmartly(items) {
    items.forEach(item => {
      const vid = item.id;
      const title = item.snippet.title;
      const artist = item.snippet.channelTitle;
      const published = timeAgo(item.snippet.publishedAt);
      const thumbnailUrl = item.snippet.thumbnails.high.url;
      const viewCount = item.statistics ? formatViews(item.statistics.viewCount) : 'N/A';

      const waveformBars = generateFakeWaveform().map(h =>
        `<div class="wave-bar" style="height:${h}px"></div>`
      ).join('');

      const container = document.createElement('div');
      container.id = `container-${vid}`;
      container.className = 'song-card';
      container.innerHTML = `
        <img src="${thumbnailUrl}" class="song-thumbnail"/>
        <div class="song-info">
          <h3>${title}</h3>
          <p class="song-artist">Artist: ${artist}</p>
          <p>${published}</p>
          <p>Views: ${viewCount}</p>
          <div class="waveform">${waveformBars}</div>
          <div id="player-${vid}" class="player-placeholder"></div>
          <div class="controls">
            <button data-action="togglePlay" data-vid="${vid}" class="btn-play">▶️</button>
            <button data-action="mute" data-vid="${vid}" class="btn-mute">🔇</button>
            <br/>
            <input type="range" class="seek-bar" data-vid="${vid}" value="0" min="0" max="0">
          </div>
        </div>
      `;

      resultsDiv.appendChild(container);

      const cfg = { elementId: `player-${vid}`, videoId: vid };
      if (window.YT && YT.Player) _initPlayer(cfg);
      else queue.push(cfg);

      container.querySelector('.controls').addEventListener('click', e => {
        const btn = e.target;
        const action = btn.getAttribute('data-action');
        const id = btn.getAttribute('data-vid');
        const pl = players[id];
        if (!pl) return;

    if (action === 'togglePlay') {
      // ✅ Pause all other players first
      Object.keys(players).forEach(pid => {
        if (pid !== id && players[pid].getPlayerState() === YT.PlayerState.PLAYING) {
          players[pid].pauseVideo();
          // ✅ Also update their play button icon back to ▶️
          const otherBtn = document.querySelector(`button[data-action="togglePlay"][data-vid="${pid}"]`);
          if (otherBtn) otherBtn.innerHTML = "▶️";
        }
      });

      const playerState = pl.getPlayerState();
      if (playerState === YT.PlayerState.PLAYING) {
        pl.pauseVideo();
        btn.innerHTML = "▶️";
      } else {
        pl.playVideo();
        btn.innerHTML = "⏸️";
        updateBottomPlayer(item);
      }
    }

        if (action === 'mute') {
          if (pl.isMuted()) {
            pl.unMute();
            btn.innerHTML = "🔊";
          } else {
            pl.mute();
            btn.innerHTML = "🔇";
          }
        }
      });

      totalLoaded++;
    });
  }

  function storeSong(items) {
    const formattedItems = items.map(item => ({
      sid: item.id,
      auID: item.snippet.channelId,
      aid: "",
      audioUrl: item.snippet.title,
      categories: (item.snippet.tags || []).join(','),
      lyrics: "",
      description: item.snippet.description,
      hashtag: "",
      thumbnailUrl: item.snippet.thumbnails.high.url,
      viewCount: item.statistics ? item.statistics.viewCount : 0
    }));

    fetch("http://127.0.0.1:1212/api/song/insert", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formattedItems),
    })
      .then(response => {
        if (!response.ok) throw new Error("Failed to store songs");
        return response.json();
      })
      .then(data => console.log("Songs stored successfully:", data))
      .catch(error => console.error("Error storing songs:", error));
  }

  function timeAgo(dateString) {
    const now = new Date();
    const past = new Date(dateString);
    const diff = now - past;
    const seconds = Math.floor(diff / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    const months = Math.floor(days / 30);
    const years = Math.floor(months / 12);

    if (years > 0) return years + " year" + (years > 1 ? "s" : "") + " ago";
    if (months > 0) return months + " month" + (months > 1 ? "s" : "") + " ago";
    if (days > 0) return days + " day" + (days > 1 ? "s" : "") + " ago";
    if (hours > 0) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
    if (minutes > 0) return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
    return seconds + " second" + (seconds > 1 ? "s" : "") + " ago";
  }

  function generateFakeWaveform(numBars = 50) {
    const bars = [];
    for (let i = 0; i < numBars; i++) {
      const ratio = 1 - i / numBars;
      const height = Math.random() * 40 * ratio + 10;
      bars.push(height);
    }
    return bars;
  }

  // =========================
  // BOTTOM PLAYER LOGIC
  // =========================

  const bottomPlayer = {
    currentSong: null,
    currentPlayer: null, // ✅ link to actual YT player
    isPlaying: false,
    intervalId: null
  };

  const playBtn = document.getElementById('play-pause');
  const seekBar = document.getElementById('seek-bar');
  const currentTimeSpan = document.getElementById('current-time');
  const durationSpan = document.getElementById('duration');
  const volumeBar = document.getElementById('volume-bar');
  const muteBtn = document.getElementById('mute');

  playBtn.addEventListener('click', () => {
    const pl = bottomPlayer.currentPlayer;
    if (!pl) return;

    const state = pl.getPlayerState();
    if (state === YT.PlayerState.PLAYING) {
      pl.pauseVideo();
      playBtn.innerHTML = '▶️';
      bottomPlayer.isPlaying = false;
      clearInterval(bottomPlayer.intervalId);
    } else {
      pl.playVideo();
      playBtn.innerHTML = '⏸️';
      bottomPlayer.isPlaying = true;
      bottomPlayer.intervalId = setInterval(updateProgress, 1000);
    }
  });

  seekBar.addEventListener('input', () => {
    const pl = bottomPlayer.currentPlayer;
    if (!pl) return;
    pl.seekTo(seekBar.value, true);
  });

  volumeBar.addEventListener('input', () => {
    const pl = bottomPlayer.currentPlayer;
    if (!pl) return;
    pl.setVolume(volumeBar.value);
  });

  muteBtn.addEventListener('click', () => {
    const pl = bottomPlayer.currentPlayer;
    if (!pl) return;
    if (pl.isMuted()) {
      pl.unMute();
      muteBtn.innerHTML = '🔊';
    } else {
      pl.mute();
      muteBtn.innerHTML = '🔇';
    }
  });

  function updateProgress() {
    const pl = bottomPlayer.currentPlayer;
    if (!pl) return;
    const current = pl.getCurrentTime();
    const duration = pl.getDuration();
    seekBar.max = Math.floor(duration);
    seekBar.value = Math.floor(current);
    currentTimeSpan.innerHTML = formatTime(current);
    durationSpan.innerHTML = formatTime(duration);
  }

  function formatTime(sec) {
    sec = Math.floor(sec);
    const m = Math.floor(sec / 60);
    const s = sec % 60;
    return m + ':' + (s < 10 ? '0' + s : s);
  }

  function updateBottomPlayer(song) {
    bottomPlayer.currentSong = song.id;
    bottomPlayer.currentPlayer = players[song.id]; // ✅ set currentPlayer
    document.querySelector('.player-thumbnail').src = song.snippet.thumbnails.default.url;
    document.querySelector('.player-title').innerHTML = song.snippet.title;
    document.querySelector('.player-artist').innerHTML = song.snippet.channelTitle;
    playBtn.innerHTML = '▶️';
    seekBar.value = 0;
    currentTimeSpan.innerHTML = '0:00';
    durationSpan.innerHTML = '0:00';
  }

  window.addEventListener('scroll', () => {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 100) {
      if (!isLoading && totalLoaded < maxItems && currentQuery) {
        searchYouTube(currentQuery);
      }
    }
  });
});
