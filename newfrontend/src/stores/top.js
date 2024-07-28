import { defineStore } from 'pinia';
import * as api from 'src/utils/api';
import { i18n } from 'boot/i18n.js';

export const useTopStore = defineStore('top', {
  state: () => ({
    shortenUrl: ''
  }),
  actions: {
    async GET_SHORTEN_URL(payload) {
      try {
        const result = await api.API_GET_SHORT_URL({
          originalUrl: payload.originalUrl
        });
        this.shortenUrl = result.data.data.shortUrl || '';
      } catch (error) {
        throw error;
      }
    },
    async INPUT_RULE_NOT_BLANK(payload) {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve((payload.val !== undefined && payload.val !== '') ||
                  i18n.global.t('rules.notBlank'));
        }, 300);
      });
    },
    async INPUT_RULE_LENGTH(payload) {
      return new Promise((resolve) => {
        setTimeout(() => {
          resolve(!(payload.val.length > 3000) ||
                  i18n.global.t('rules.UrlLength'));
        }, 300);
      });
    }
  },
});
