import { useI18n } from 'vue-i18n'
import { i18n } from 'boot/i18n.js'

export const GET_ERROR_DIALOG_MESSAGE = (error) => {
  if (error && error.data && error.data.status === 'error') {
    return {
      title: error.data.message,
      message: error.data.error.description,
      buttonLabels: i18n.global.t('api.serverError.buttonLabels')
    }
  } else {
    return {
      title: i18n.global.t('api.error.title'),
      message: i18n.global.t('api.error.message'),
      buttonLabels: i18n.global.t('api.error.buttonLabels')
    }
  }
}
