<template>
    <Modal class-name="vertical-center-modal" draggable scrollable v-model="showModal" :width="modalWidth"
        :title="modalTitle">
        <iframe v-if="showIframe" style="width:100%; height:660px;" :src="iframeSrc" frameborder="no"
            border="0"></iframe>
        <component v-if="!showIframe" :key="modalComponentKey" :is="modalComponent"></component>
    </Modal>
</template>
<script>
    import {
        MenuEntryProtEnum
    } from "../../libs/fw-core/fw-constant/constant";
    export default {
        components: {

        },
        data() {
            return {

                showModal: true,
                showIframe: false,

                modalTitle: "",
                modalComponent: "",
                modalComponentKey: "",
                modalWidth: "1100",

                iframeSrc: ""

            };
        },
        mounted() {

        },
        methods: {

            openPage(menu) {

                this.showModal = true;
                this.modalTitle = menu.menuName;

                if (MenuEntryProtEnum.INTERNET == menu.menuEntryProtCode) {

                    this.showIframe = true;
                    this.iframeSrc = menu.menuEntryProtContent;

                } else if (MenuEntryProtEnum.COMPONENT == menu.menuEntryProtCode) {

                    this.showIframe = false;
                    this.modalComponentKey = menu.id;
                    this.modalComponent = this.$store.state.menuComponent[menu.id].default;

                }

            }

        }
    };
</script>
<style lang="less">

</style>