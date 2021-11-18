import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const types ={
  SET_AUTHENTICATED: 'SET_AUTHENTICATE',
  SET_USER: 'SET_USER',
  SET_TOKEN: 'SET_TOKEN'
};

const state = {
  isAuthenticated: window.sessionStorage.getItem("isAuthenticated"),
  user: JSON.parse(window.sessionStorage.getItem("user")),
  token: window.sessionStorage.getItem("token")
};

const getters ={
  isAuthenticated: state =>state.isAuthenticated,
  user: state => state.user,
  token: state => state.token
};

const mutations ={
  [types.SET_AUTHENTICATED](state,isAuthenticated){      /*设置是否授权*/
    state.isAuthenticated = isAuthenticated;
    window.sessionStorage.setItem("isAuthenticated", isAuthenticated);
  },
  /*类型，参数*/
  [types.SET_USER](state,user){
    state.user = user;
    window.sessionStorage.setItem("user", JSON.stringify(user));
  },
  [types.SET_TOKEN](state,token){
    state.token = token;
    window.sessionStorage.setItem("token", token);
  }
};

/*异步操作  调用mutations*/
const actions ={
  setAuthenticated:( {commit},isAuthenticated) =>{
    commit(types.SET_AUTHENTICATED,isAuthenticated);
  },
  setUser:({commit},user) =>{
    commit(types.SET_USER,user);
  },
  setToken:({commit},token) =>{
    commit(types.SET_TOKEN,token);
  }
};

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
});