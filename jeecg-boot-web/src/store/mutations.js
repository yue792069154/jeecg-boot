export const ACCESS_TOKEN = 'Access-Token';
export const ENCRYPTED_STRING = 'ENCRYPTED_STRING';

export const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token;
    }
};