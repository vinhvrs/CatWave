var API_KEY="";

onload = async function(){
    await fetch("http://127.0.0.1:1212/api/api_key", {
        // type: "GET",
        // headers: {
        //     "Content-Type": "application/json",
        //     "Accept": "application/json"
        // }
    }).then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Network response was not ok');
        }
    })
    .then(data => {
        API_KEY = data;
    });
}
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('searchForm');
    const input = document.getElementById('searchInput');
    const resultsDiv = document.getElementById('results');

    let currentQuery = "";
    let nextPageToken = "";
    let totalLoaded = 0;
    const maxItems = 50; 
    const pageSize = 10;
    let isLoading = false;

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        resultsDiv.innerHTML = "";
        currentQuery = input.value.trim();
        nextPageToken = "";
        totalLoaded = 0;
        if (currentQuery) {
            searchYouTube(currentQuery);
        }
    });

    async function searchYouTube(query) {
        if (totalLoaded >= maxItems || isLoading) return;

        isLoading = true;
        let url = `https://www.googleapis.com/youtube/v3/search?part=snippet&type=playlist&maxResults=${pageSize}&q=${encodeURIComponent(query)}&key=${API_KEY}`;
        if (nextPageToken) {
            url += `&pageToken=${nextPageToken}`;
        }

        try {
            const response = await fetch(url);
            const data = await response.json();

            if (data.items && data.items.length > 0) {
                insertVideosSmartly(data.items);
                nextPageToken = data.nextPageToken || "";
            }
        } catch (error) {
            console.error('Fetch error:', error);
        } finally {
            isLoading = false;
        }
    }

    function insertVideosSmartly(items) {
        let index = 0;

        function loadNext() {
            if (index >= items.length || totalLoaded >= maxItems) {
                return;
            }

            const item = items[index];
            const playlistId = item.id.playlistId;
            const title = item.snippet.title;

            const iframe = document.createElement('iframe');
            iframe.width = "320";
            iframe.height = "180";
            iframe.src = `https://www.youtube.com/embed/videoseries?list=${playlistId}`;
            iframe.frameBorder = "0";
            iframe.allowFullscreen = true;
            iframe.loading = "lazy";

            const container = document.createElement('div');
            container.style.marginBottom = '20px';
            container.innerHTML = `<p><strong>${title}</strong></p>`;
            container.appendChild(iframe);

            resultsDiv.appendChild(container);

            iframe.addEventListener('error', async function() {
                console.warn(`Video failed to load, removing...`);

                resultsDiv.removeChild(container);
                totalLoaded--; // Adjust because this iframe failed

                // Immediately fetch more to fill missing one
                if (currentQuery && totalLoaded < maxItems && !isLoading) {
                    await searchYouTube(currentQuery);
                }
            });

            totalLoaded++;
            index++;

            if (index < items.length && totalLoaded < maxItems) {
                setTimeout(loadNext, 300);
            }
        }

        loadNext();
    }

    window.addEventListener('scroll', function() {
        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 100) {
            if (currentQuery && totalLoaded < maxItems) {
                searchYouTube(currentQuery);
            }
        }
    });
});
