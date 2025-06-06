var API_KEY = "";
const players = {};      // map videoId → YT.Player
const queue = [];      // queue before API ready

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

// When a player is ready, set up its seek bar
function onPlayerReady(event, videoId) {
  const player = event.target;
  const container = document.getElementById(`container-${videoId}`);
  const seekBar = container.querySelector('.seek-bar');

  // once we know duration, set max
  const duration = player.getDuration();
  seekBar.max = Math.floor(duration);

  // update slider as video plays
  setInterval(() => {
    if (player && player.getCurrentTime) {
      seekBar.value = Math.floor(player.getCurrentTime());
    }
  }, 500);

  // scrub when user drags
  seekBar.addEventListener('input', e => {
    player.seekTo(Number(e.target.value), true);
  });
}

// (optional) handle state changes if you like
function onPlayerStateChange(event) {
  // e.g. you could update a Play/Pause icon here
}

onload = function () {
  this.fetch(`/api/session/validateCookie`, {
      method: "GET",
      credentials: "include",
    }).then((res) => {
      if (res.ok) {
        const data = res.json();
        this.localStorage.setItem("uid", data.uid);
        this.localStorage.setItem("username", data.username);

        // redirect to home if already logged in
        window.location.href = "/home";
      } else if (res.status === 401) {
        console.log("No valid session cookie found, proceeding to login.");
      }
    });
}

// Fetch API_KEY
onload = async function () {
  try {
    const res = await fetch(`http://127.0.0.1:1212/api/api_key`);
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

    const url = new URL(`https://www.googleapis.com/youtube/v3/search`);
    url.searchParams.set("part", "snippet");
    url.searchParams.set("type", "list");
    url.searchParams.set("maxResults", pageSize);
    url.searchParams.set("videoEmbeddable", "true");
    url.searchParams.set("q", query);
    url.searchParams.set("key", API_KEY);
    if (nextPageToken) url.searchParams.set("pageToken", nextPageToken);

    try {
      const res = await fetch(url);
      const data = await res.json();
      // 1) Filter out non-video results
      const items = (data.items || [])
        .filter(item => item.id.kind === "youtube#video");

      // 2) Only proceed if we actually got some
      if (items.length > 0) {
        insertVideosSmartly(items);
        storeSong(items);

        // 3) Pull the nextPageToken from the original data
        nextPageToken = data.nextPageToken || "";
      }
    } catch (err) {
      console.error("Fetch error:", err);
    } finally {
      isLoading = false;
    }
  }

  function insertVideosSmartly(items) {
    let idx = 0;

    function loadNext() {
      if (idx >= items.length || totalLoaded >= maxItems) return;
      const item = items[idx++];
      const vid = item.id.videoId;
      const title = item.snippet.title;

      // container with unique ID
      const container = document.createElement('div');
      container.id = `container-${vid}`;
      container.className = 'video-container';
      container.innerHTML = `<p><strong>${title}</strong></p>`;

      // hidden placeholder for YT.Player
      const ph = document.createElement('div');
      ph.className = 'player-placeholder';
      ph.id = `player-${vid}`;
      container.appendChild(ph);

      // custom controls + seek bar
      const controls = document.createElement('div');
      controls.className = 'controls';
      controls.innerHTML = `
        <button data-action="play"  data-vid="${vid}">▶️ Play</button>
        <button data-action="pause" data-vid="${vid}">⏸ Pause</button>
        <button data-action="mute"  data-vid="${vid}">🔇 Mute</button>
        <br/>
        <input type="range" class="seek-bar" data-vid="${vid}" value="0" min="0" max="0">
      `;
      container.appendChild(controls);

      resultsDiv.appendChild(container);

      // instantiate or queue the player
      const cfg = { elementId: ph.id, videoId: vid };
      if (window.YT && YT.Player) _initPlayer(cfg);
      else queue.push(cfg);

      // wire play/pause/mute
      controls.addEventListener('click', e => {
        const btn = e.target;
        const action = btn.getAttribute('data-action');
        const id = btn.getAttribute('data-vid');
        const pl = players[id];
        if (!pl) return;
        if (action === 'play') pl.playVideo();
        if (action === 'pause') pl.pauseVideo();
        if (action === 'mute') pl.isMuted() ? pl.unMute() : pl.mute();
      });

      totalLoaded++;
      if (idx < items.length && totalLoaded < maxItems) {
        setTimeout(loadNext, 300);
      }
    }

    loadNext();
  }
}) 