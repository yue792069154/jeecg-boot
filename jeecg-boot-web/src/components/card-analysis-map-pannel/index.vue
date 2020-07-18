<template>
    <div id="sc-esa-map-card" class="animated fadeIn">
        <div class="sc-esa-map-card-pannel">
            <div class="sc-esa-map-card-header-panel">

            </div>
            <div class="sc-esa-map-card-content-panel">
                <slot></slot>
            </div>
            <div class="sc-esa-map-card-footer-panel">

            </div>
            <div class="sc-esa-map-card-border-l"></div>
            <div class="sc-esa-map-card-border-r"></div>
            <div class="sc-esa-map-card-border-t"></div>
            <div class="sc-esa-map-card-border-b"></div>
        </div>
    </div>
</template>
<script>
    export default {
        components: {

        },
        props: {

        },
        data() {
            return {
                showHeader: true
            }
        },
        mounted() {
            this.onSetHeader();
        },
        methods: {

            onBreadCrumbSelect(item, index) {
                this.breadcrumbList.splice(index + 1, this.breadcrumbList.length - 1);
                this.$emit('on-bread-crumb-select', item);
            },
            onSetHeader() {
                if (!ObjectUtils.hasValue(this.title)) {
                    if (!this.breadcrumb) {
                        this.showHeader = false;
                    };
                };
            }
        }
    };
</script>
<style lang="less" scoped>
    @font-face {
        font-family: 'LCD';
        src: url('./assets/font/LCD.ttf');
    }

    @font-face {
        font-family: 'HWXW';
        src: url('./assets/font/HWXW.ttf');
    }

    #sc-esa-map-card {

        margin-bottom: 10px;

        .sc-esa-map-card-pannel {
            padding: 10px;
            position: relative;
            width: 100%;
            height: 100%;

            min-width: 260px;
            background: rgba(8, 61, 99, 0.25);
        }

        .sc-esa-map-card-border-l {
            position: absolute;
            width: 1px;
            background-color: #3db4dd;
            height: ~"calc(100% - 38px)";
            top: 18px;
            left: -1px;
        }

        .sc-esa-map-card-border-r {
            position: absolute;
            width: 1px;
            background-color: #3db4dd;
            height: ~"calc(100% - 38px)";
            top: 18px;
            right: -1px;
        }

        .sc-esa-map-card-border-t {
            position: absolute;
            height: 1px;
            background-color: #3db4dd;
            width: ~"calc(100% - 37px)";
            top: -1px;
            left: 18px;
        }

        .sc-esa-map-card-border-b {
            position: absolute;
            height: 1px;
            background-color: #3db4dd;
            width: ~"calc(100% - 54px)";
            bottom: 0px;
            left: 18px;
        }

        .sc-esa-map-card-header-panel {

            width: 100%;
            position: relative;
            display: flex;
            align-items: center;

            &::before {
                content: '';
                position: absolute;
                width: 20px;
                height: 20px;
                background: url(./assets/images/esa-card-l-t.svg) no-repeat;
                top: -11px;
                left: -11px;
            }

            &::after {
                content: '';
                position: absolute;
                width: 20px;
                height: 20px;
                background: url(./assets/images/esa-card-r-t.svg) no-repeat;
                top: -11px;
                right: -11px;
            }

            .sc-esa-map-card-header {
                color: white;
                font-size: 2.5rem;
                background-image: url(./assets/images/esa-card-header-logo.svg);
                background-repeat: no-repeat;
                background-position: left center;
                padding: 0px 0px 0px 20px;
                text-shadow: 0 0 0px #fff,
                    0 0 3px #fff,
                    0 0 10px #87d9ff;
            }

            .sc-esa-map-card-header-date {
                font-family: 'LCD';
                margin-left: 10px;
                font-size: 2.4rem;
                color: #00E6FF;
            }
        }

        .sc-esa-map-card-content-panel {
            width: 100%;
            overflow: hidden;
            font-family: 'LCD';
            height: ~"calc(100% - 0px)";
        }

        .sc-esa-map-card-footer-panel {

            width: 100%;
            position: relative;

            &::before {
                content: '';
                position: absolute;
                width: 20px;
                height: 20px;
                background: url(./assets/images/esa-card-l-b.svg) no-repeat;
                top: -10px;
                left: -11px;
            }

            &::after {
                content: '';
                position: absolute;
                width: 38px;
                height: 38px;
                background: url(./assets/images/esa-card-r-b.svg) no-repeat;
                top: -28px;
                right: -11px;
            }
        }


    }
</style>