import axios from 'axios'

import { getBackendUrl } from 'boot/axios';

const withCredentials = true

const timeout = 300 * 1000

/* ------ request ------ */
const request = (payload) => {
  const backendUrl = getBackendUrl()
  const config = {
    url: `${backendUrl}${payload.url}`,
    method: payload.method,
    xhrFields: {
      withCredentials: withCredentials
    },
    json: true,
    timeout,
    headers:
      {
        'Content-Type': 'application/json'
      }
  }

  if (payload.method === 'GET') {
    config.params = payload.params
  } else if (payload.method === 'POST') {
    config.data = payload.data
  }
  return axios(config)
    .then((result) => {
      if (result && result.data && result.data.status === 'error') {
        throw result
      }
      return result
    }).catch((error) => {
      throw error
    })
}

// shorten original url -> get short url
export const API_GET_SHORT_URL = async (payload) => {
  const url = `/shorten`;
  const method = 'POST'

  const postData = {
    originalUrl: payload.originalUrl
  }
  const data = JSON.stringify(postData)
  return request({
    url,
    method,
    data
  }).then((result) => {
    return result
  }).catch((error) => {
    throw error
  })
}
