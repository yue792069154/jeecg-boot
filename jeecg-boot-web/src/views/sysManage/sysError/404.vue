<template>
    <div class="error">
        <div class="error-form">
            404
        </div>
        <div class="error-button" @click="onGoHome">
            返回首页
            <Icon type="md-share-alt" />
        </div>
    </div>
</template>

<script>
    import Vue from 'vue';
    import verificationCode from "../../../components/verification-code";
    import {
        ObjectUtils
    } from "../../../libs/fw-core/fw-util";

    export default {
        components: {
            verificationCode
        },
        data() {
            return {

            }
        },
        created() {

        },
        methods: {
            onGoHome() {
                this.$router.push('/system');
            }
        }

    }
</script>

<style lang="less" scoped>
    .error {

        width: 100%;
        min-height: 100%;
        background: #f0f2f5 url(../../../assets/images/svgs/login_background.svg) no-repeat 50%;
        background-size: 100%;
        padding: 110px 0 144px;
        position: relative;



        .error-form {
            margin: 0 auto;
            width: 400px;
            text-align: center;
            color: #BABABA;
            font-size: 180px;
            vertical-align: middle;
            opacity: 0.94;
            border-radius: 8px;
            padding: 30px;
            box-sizing: border-box;
            position: relative;

        }

        .error-button {
            background: #BACAD9;
            width: 140px;
            border: 2px solid white;
            height: 40px;
            text-align: center;
            cursor: pointer;
            line-height: 36px;
            margin: 0 auto;
        }



    }
</style>