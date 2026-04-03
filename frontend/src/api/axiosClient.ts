import axios from "axios";

const axiosClient = axios.create({
  baseURL: (import.meta as any).env.VITE_API_URL,
  headers: {
    "Content-Type": "application/json"
  }
});

export default axiosClient;