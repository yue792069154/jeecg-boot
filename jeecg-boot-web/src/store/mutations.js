export const ACCESS_TOKEN = 'Access-Token';
export const ENCRYPTED_STRING = 'ENCRYPTED_STRING';
export const USER_MENU_LIST = 'LOGIN_USER_MENU_LIST';

export const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token;
    },
    SET_USER_MENU_LIST: (state, menuList) => {
        state.userMenuList = menuList;
    },
    SET_MENU_COMPONENT(state, data) {
        state.menuComponent = data.menuComponent;
    },
};