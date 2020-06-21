<template>
    <div style="width:100%; height:100%;">
        <router-view v-show="!showIframe"></router-view>
        <iframe v-show="showIframe" style="width:100%; height:100%;" :src="iframeSrc" frameborder="no"
            border="0"></iframe>
    </div>
</template>
<script>
    import {
        MenuEntryProtEnum
    } from "../../libs/fw-core/fw-constant/constant"
    export default {
        data() {
            return {

                showIframe: false,
                iframeSrc: ""

            };
        },
        mounted() {

        },
        methods: {

            openPage(menu) {

                this.showIframe = true;

                if (MenuEntryProtEnum.INTERNET == menu.menuEntryProtCode) {

                    this.showIframe = true;
                    this.iframeSrc = menu.menuEntryProtContent;

                } else if (MenuEntryProtEnum.COMPONENT == menu.menuEntryProtCode) {

                    this.showIframe = false;
                    this.$router.push('/system/' + (menu.menuRouteContent || menu.id));

                }

            }

        }
    };
</script>
<style lang="less">

</style>