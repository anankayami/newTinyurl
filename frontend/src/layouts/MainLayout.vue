<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-toolbar-title>
          <div class="text-h5 text-center">
            {{ $t('title') }}
          </div>
        </q-toolbar-title>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view to="/login" />
    </q-page-container>

  </q-layout>
</template>

<script>
export default {
  name: 'MainLayout'
}
</script>

<style>
</style>
