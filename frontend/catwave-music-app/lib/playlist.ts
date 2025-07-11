import api from "./api";

export const getPlaylists = async (uid: string) => {
  try {
    const res = await api.get(`/${uid}/playlists`);
    return res.data;
  } catch (err) {
    console.error("Failed to load playlists", err);
    return [];
  }
};
