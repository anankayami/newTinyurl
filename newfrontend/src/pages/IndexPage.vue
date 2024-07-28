<template>
  <q-page>
    <div class="layout-padding row justify-center background-whitesmoke items-center">
      <div class="col-xs-12 col-sm-7 col-lg-5">
        <q-card class="q-card-background-white">
          <q-form @submit="onSubmit" @reset="onReset" class="bg-grey-1 q-gutter-auto" autocomplete="off">
            <div class="row">
              <div class="col-sm-8 offset-xs-2 text-center">
                <h5>Shorten your URL</h5>
              </div>
            </div>
            <div class="row">
              <div class="col-sm-8 offset-xs-2">
                <q-input
                  v-model="originUrl"
                  type="textarea"
                  label="Please input your original URL:"
                  :rules="[inputShortenerRule, inputUrlLengthRule]"
                  autocomplete="off"
                  color="primary"
                >
                  <template v-if="originUrl" v-slot:append>
                    <q-icon name="close" @click="onReset" class="cursor-pointer" />
                  </template>
                </q-input>
              </div>
            </div>
            <br />
            <div class="row">
              <div class="col-sm-8 offset-xs-2">
                <q-card>
                  <q-card-section class="row items-center">
                    <div class="col-sm-8 offset-xs-0 text-left">
                      <q-avatar icon="link" color="primary" text-color="white" />
                    </div>
                    <div class="col-sm-8 offset-xs-2 text-right">
                      <a v-if="confirm" :href="shortUrl" target="_blank">{{ shortUrl }}</a>
                    </div>
                  </q-card-section>
                </q-card>
              </div>
            </div>
            <br />
            <div class="row">
              <div class="col-sm-8 offset-xs-2"></div>
            </div>
            <div class="row" style="padding-top: 20px;">
              <div class="col-sm-8 offset-xs-2 text-right">
                <q-btn color="primary" style="font-weight: bold;" type="submit">Shorten</q-btn>
                <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
              </div>
            </div>
            <br />
          </q-form>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script>
import { ref, watch } from 'vue';
import { useQuasar } from 'quasar';
import { useTopStore } from '../stores/top';
import { GET_ERROR_DIALOG_MESSAGE } from 'src/const/utils/apiConst';

export default {
  setup() {
    const $q = useQuasar();
    const topStore = useTopStore();
    const originUrl = ref('');
    const shortUrl = ref('');
    const confirm = ref(false);

    watch(originUrl, (newVal, oldVal) => {
      if (oldVal !== '' && newVal.length !== oldVal.length && shortUrl.value !== '') {
        shortUrl.value = '';
      }
    });

    const onSubmit = async () => {
      confirm.value = true;
      try {
        await topStore.GET_SHORTEN_URL({ originalUrl: originUrl.value });
        shortUrl.value = topStore.shortenUrl;
      } catch (error) {
        const dialog = GET_ERROR_DIALOG_MESSAGE(error);
        $q.dialog({
          title: dialog.title,
          message: dialog.message,
          buttonLabels: dialog.buttonLabels
        });
      }
    };

    const onReset = () => {
      originUrl.value = '';
      shortUrl.value = '';
      confirm.value = false;
    };

    const inputShortenerRule = (val) => topStore.INPUT_RULE_NOT_BLANK({ val });
    const inputUrlLengthRule = (val) => topStore.INPUT_RULE_LENGTH({ val });

    return {
      originUrl,
      shortUrl,
      confirm,
      onSubmit,
      onReset,
      inputShortenerRule,
      inputUrlLengthRule
    };
  }
};
</script>

<style scoped lang="stylus">
.q-card-background-white {
  background: white;
}

.background-whitesmoke {
  background: whitesmoke;
  height: 80vh;
}
</style>
