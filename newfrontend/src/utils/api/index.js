import { axiosInstance } from 'boot/axios';

const timeout = 300 * 1000;

/* ------ request ------ */
const request = async (payload) => {
  const config = {
    url: `${payload.url}`,
    method: payload.method,
    timeout,
    headers: {
      'Content-Type': 'application/json'
    },
    withCredentials: true,
  };

  if (payload.method === 'GET') {
    config.params = payload.params;
  } else if (payload.method === 'POST') {
    config.data = payload.data;
  }

  return axiosInstance(config)
    .then((result) => {
      if (result && result.data && result.data.status === 'error') {
        throw result;
      }
      return result;
    }).catch((error) => {
      throw error;
    });
}

// shorten original url -> get short url
export const API_GET_SHORT_URL = async (payload) => {
  const url = '/shorten'; // Endpoint URL
  const method = 'POST';

  const postData = {
    originalUrl: payload.originalUrl
  };

  const data = JSON.stringify(postData);

  return request({
    url,
    method,
    data
  }).then((result) => {
    return result;
  }).catch((error) => {
    throw error;
  });
}
