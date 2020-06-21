import Vue from 'vue'

const getters = {
 
  userInfo: state => state.userInfo,
  token: state => state.token,
  userMenuList: state => state.userMenuList

};

export default getters;