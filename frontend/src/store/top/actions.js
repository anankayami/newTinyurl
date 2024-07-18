import * as api from 'src/utils/api'

import { i18n } from 'boot/i18n.js'

export async function GET_SHORTEN_URL ({ commit }, payload) {
  await api.API_GET_SHORT_URL({
    originalUrl: payload.originalUrl
  }).then((result) => {
    commit('SET_SHORTEN_URL', {
      shortenUrl: result.data.data.shortUrl || ''
    })
  }).catch((error) => {
    throw error
  })
}

export async function INPUT_RULE_NOT_BLANK ({ commit }, payload) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve((payload.val !== undefined && payload.val !== '') ||
        i18n.t('rules.notBlank'))
    }, 300)
  })
}

export async function INPUT_RULE_LENGTH ({ commit }, payload) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve(!(payload.val.length > 3000) ||
        i18n.t('rules.UrlLength'))
    }, 300)
  })
}
