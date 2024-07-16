import axios from 'axios'

import {
  BACKEND_URL
} from 'src/const/utils/apiConst'

// dev環境ではtrue
const withCredentials = process.env.DEV

const timeout = 300 * 1000

/* ------ request ------ */
const request = (payload) => {
  const config = {
    url: payload.url,
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
  const url = `${BACKEND_URL}/api/url/shorten`
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
