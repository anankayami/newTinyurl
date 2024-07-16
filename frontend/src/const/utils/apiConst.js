import { i18n } from 'boot/i18n.js'

import setting from 'app/default.json'

export const BACKEND_URL = setting.url.backendShorten

/* ------ api error message ------ */
export const GET_ERROR_DIALOG_MESSAGE = (error) => {
  if (error && error.data && error.data.status === 'error') {
    return {
      title: error.data.message,
      message: error.data.error.description,
      buttonLabels: i18n.t('api.serverError.buttonLabels')
    }
  } else {
    return {
      title: i18n.t('api.error.title'),
      message: i18n.t('api.error.message'),
      buttonLabels: i18n.t('api.error.buttonLabels')
    }
  }
}
