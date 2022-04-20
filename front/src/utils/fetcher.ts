import axios from "axios";

const fetcher = async (url : string) => {
  const res = await axios.get(url, {
    withCredentials: true,
  });
  return res.data;
}

export default fetcher;