import axiosClient from "./axiosClient";

export const getConnection = async (from: string, to: string) => {
  const response = await axiosClient.get("/api/connection", {
    params: { from, to }
  });

  return response.data;
};