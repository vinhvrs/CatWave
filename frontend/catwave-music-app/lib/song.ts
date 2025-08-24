import api from "./api";

export const getAllSongs = async () => {
  try {
    const res = await api.get("/songs");
    console.log("Fetched songs from API:", res.data);

    const transformed = res.data.map((song: any) => ({
      id: song.sid, // ✅ map sid ➔ id
      title: song.audiourl,
      artist: "Unknown Artist",
      thumbnail: song.thumbnailurl || "/placeholder.svg", // ✅ map thumbnailUrl ➔ thumbnail
      duration: "3:00",
      isPlaying: false,
      videoId: song.videoid || "", // nếu backend có field này
    }));

    console.log("Transformed songs:", transformed);
    return transformed;
  } catch (err) {
    console.error("Failed to load songs", err);
    return [];
  }
};

export const searchYouTube = async (query: string) => {
  const apiKey = process.env.NEXT_PUBLIC_YOUTUBE_API_KEY;

  console.log("YouTube API KEY:", apiKey);

  if (!apiKey) throw new Error("Missing YouTube API Key");

  // Step 1: Search
  const searchUrl = `https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=10&q=${encodeURIComponent(query)}&key=${apiKey}`;
  const searchRes = await fetch(searchUrl);
  const searchData = await searchRes.json();

  const videoIds = searchData.items.map((item: any) => item.id.videoId).join(",");
  if (!videoIds) return [];

  // Step 2: Get video details (statistics, thumbnails, duration if needed)
  const videosUrl = `https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics&id=${videoIds}&key=${apiKey}`;
  const videosRes = await fetch(videosUrl);
  const videosData = await videosRes.json();

  // Step 3: Transform data
  const results = videosData.items.map((item: any) => ({
    sid: item.id,
    title: item.snippet.title,
    artist: item.snippet.channelTitle,
    thumbnail: item.snippet.thumbnails.high.url,
    duration: "3:00", // YouTube Data API v3 không trả duration ở đây (cần videos?part=contentDetails)
    viewCount: item.statistics?.viewCount || 0,
    videoId: item.id,
    isPlaying: false,
  }));

  console.log("YouTube search transformed results:", results);
  return results;
};

