<template>
    <div class="login">
        <div class="login-form">
            
            <div class="login-form-title">
                <div>上泰电气智能接地箱软件系统</div>
                <div style="font-size:18px;margin-top:10px">后台登陆</div>
            </div>
            <div class="login-form-item">
                <Icon type="md-person" size="23" class="login-form-item-icon" />
                <input class="login-form-item-input" v-on:blur="onInputBlur" @keyup.enter="onLogin" v-model="loginForm.userName"
                    placeholder="请输入登陆用户账户" type="text">
                <div class="login-form-item-error" v-if="showUserError">
                    请正确输入登陆用户账户
                </div>
            </div>
            <div class="login-form-item">
                <Icon type="md-lock" size="23" class="login-form-item-icon" />
                <input class="login-form-item-input" v-on:blur="onInputBlur" @keyup.enter="onLogin" v-model="loginForm.password"
                    placeholder="请输入登陆用户密码" type="password">
                <div class="login-form-item-error" v-if="showPasswordError">
                    请正确输入登陆用户密码
                </div>
            </div>
            <div class="login-form-item">
                <Icon type="md-barcode" size="23" class="login-form-item-icon" />
                <input class="login-form-item-input" v-on:blur="onInputBlur" @keyup.enter="onLogin" v-model="loginForm.captcha"
                    placeholder="请输入验证码" type="text">
                <verification-code class="login-form-item-code" :remote="true" ref="verificationCode">
                </verification-code>
                <div class="login-form-item-error" v-if="showCodeError">
                    请正确输入验证码
                </div>
            </div>
            <div class="login-form-item-btn">
                <Button size="large" :loading="showLoading" type="primary" long @click="onLogin">立即登陆</Button>
            </div>
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

                showLoading: false,
                showUserError: false,
                showPasswordError: false,
                showCodeError: false,

                loginForm: {
                    userName: "wangzy",
                    password: "Wangzy@wangzy123",
                    captcha: ""
                }
            }
        },
        created() {

        },
        methods: {
            onLogin: function () {

                var vm = this;

                this.onInputBlur();

                if (ObjectUtils.hasValue(this.loginForm.userName) && ObjectUtils.hasValue(this.loginForm
                        .password) &&
                    ObjectUtils.hasValue(this.loginForm.captcha)) {

                    var checkParams = this.$refs.verificationCode.getLoginParam();
                    if (checkParams.checkCode.toLowerCase() === this.loginForm.captcha.toLowerCase()) {

                        this.showLoading = true;
                        this.showCodeError = false;

                        this.$store.dispatch('login', {
                            userName: this.loginForm.userName,
                            password: this.loginForm.password,
                            captcha: checkParams.checkCode,
                            checkKey: checkParams.checkKey
                        }).then(response => {
                            this.showLoading = false;
                            vm.$router.push('/system');
                        }).catch(error => {
                            this.showLoading = false;
                        });

                    } else {
                        this.showCodeError = true;
                    }

                    
                }
            },
            onInputBlur() {

                if (!ObjectUtils.hasValue(this.loginForm.userName)) {
                    this.showUserError = true;
                } else {
                    this.showUserError = false;
                };

                if (!ObjectUtils.hasValue(this.loginForm.password)) {
                    this.showPasswordError = true;
                } else {
                    this.showPasswordError = false;
                };

                if (!ObjectUtils.hasValue(this.loginForm.captcha)) {
                    this.showCodeError = true;
                } else {
                    this.showCodeError = false;
                };

            }
        }

    }
</script>

<style lang="less" scoped>
    input:-webkit-autofill,
    textarea:-webkit-autofill,
    select:-webkit-autofill {
        background-color: rgb(250, 255, 189);
        background-image: none;
        color: rgb(0, 0, 0);
    }

    input {
        outline: medium;
    }

    .login {

        width: 100%;
        min-height: 100%;
        background: #f0f2f5 url(../../../assets/images/login_background.svg) no-repeat 50%;
        background-size: 100%;
        padding: 110px 0 144px;
        position: relative;



        .login-form {
            margin: 0 auto;
            width: 400px;
            background-color: white;
            font-size: 14px;
            vertical-align: middle;
            opacity: 0.94;
            border-radius: 8px;
            padding: 30px;
            box-sizing: border-box;
            position: relative;
            box-shadow: 0 1px 1px rgba(0, 0, 0, 0.05), 0 2px 6px 0 rgba(0, 0, 0, 0.045);

            .login-form-item {
                height: 76px;
            }

            .login-form-title {
                font-size: 26px;
                text-align: center;
            }

            .login-form-item-icon {
                position: relative;
                top: 31px;
            }

            .login-form-item-input {

                width: 100%;
                height: 40px;
                padding: 6px 12px;
                padding-left: 36px;
                background-color: #fff;
                border: thin solid #dedee0;
                -webkit-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
                -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
                transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
                font-size: 15px;
                color: #666;
                transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
                box-sizing: border-box;
                border-top: none;
                border-left: none;
                border-right: none;

            }


            .login-form-item-error {
                height: 20px;
                line-height: 20px;
                font-size: 13px;
                color: red;
                padding-top: 2px;
            }

            .login-form-item-code {
                position: absolute;
                bottom: 100px;
                left: 235px;
            }

            .login-form-item-btn {
                margin-top: 20px;
            }
        }



    }
</style>