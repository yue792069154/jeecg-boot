import {
    axios
  } from './request';
  
  //post
  export function postAction(url, baseURL, params) {
    return axios({
      baseURL: baseURL,
      url: url,
      method: 'post',
      data: params
    });
  }
  
  //post method= {post | put}
  export function httpAction(url, baseURL, params, method) {
    return axios({
      baseURL: baseURL,
      url: url,
      method: method,
      data: params
    });
  }
  
  //put
  export function putAction(url, baseURL, params) {
    return axios({
      baseURL: baseURL,
      url: url,
      method: 'put',
      data: params
    });
  }
  
  //get
  export function getAction(url, baseURL, params) {
    return axios({
      baseURL: baseURL,
      url: url,
      method: 'get',
      params: params
    });
  }
  
  //deleteAction
  export function deleteAction(url, baseURL, params) {
    return axios({
      baseURL: baseURL,
      url: url,
      method: 'delete',
      params: params
    });
  }
  
  //downLoadFile
  export function downLoadFileAction(url, baseURL, params) {
    return axios({
      baseURL: baseURL,
      url: url,
      params: params,
      method: 'get',
      responseType: 'blob'
    });
  }