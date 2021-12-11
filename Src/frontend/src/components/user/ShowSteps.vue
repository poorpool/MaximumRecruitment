<template>
  <v-stepper alt-labels>
    <v-stepper-header>
      <template v-for="(step, index) in steps">
        <v-stepper-step
            :key="index"
            :complete="index <= nowStep"
            :step="index+1"
            :rules="[() => index < nowStep || endStatus < 1]"
        >
          {{step}}
        </v-stepper-step>

        <v-divider
            v-if="index + 1 !== steps.length"
            :key="index - steps.length - 1"
        ></v-divider>
      </template>

    </v-stepper-header>
  </v-stepper>
</template>


<script>
export default {
  name: 'ShowSteps',
  props: ['steps', 'nowStep', 'endStatus'],
  data: () => ({
  }),
  methods: {
  },
  computed: {
  }
}
</script>