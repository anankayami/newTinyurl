// This is just an example,
// so you can safely delete all default props below

export default {
  api: {
    error: {
      title: 'Error occurred',
      message: 'Please contact administrator',
      buttonLabels: 'Confirm'
    },
    serverError: {
      buttonLabels: 'Confirm'
    }
  },
  selectLang: 'Select Language',
  ja: '日本語',
  en: 'English',
  title: 'Shortener URL',
  rules: {
    notBlank: 'Required input.',
    UrlLength: 'The length of the input content is not within the range of 3000 characters.'
  }
}
