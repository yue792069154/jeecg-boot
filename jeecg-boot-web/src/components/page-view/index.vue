<template>
    <div style="width:100%; height:100%;">
        <router-view v-show="test"></router-view>
        <iframe v-show="!test" style="width:100%; height:calc(100% - 200px);" src="http://www.baidu.com" frameborder="no" border="0"
            scrolling1="no"></iframe>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                test: false

            };
        },
        mounted() {


        },
        methods: {

            openPage(menuActive) {

                var vm = this;
                if (menuActive.menuEntryProtCode == 1) {
                    this.test = false;
                } else {
                    this.test = true;
                    this.$router.push('/system/' + (menuActive.menuRouteContent || this.menuActive.id));
                }

            }
        }
    };
</script>
<style lang="less">

</style>