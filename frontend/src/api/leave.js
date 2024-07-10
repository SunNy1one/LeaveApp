import request from '../utils/request'


export const saveLeave = (data) => {
    return request({
        url: '/leave', method: 'POST', data: data
    })
}


export const removeLeaveBatchByIds = (ids) => {
    return request({
        url: `/leave/${ids}`, method: 'DELETE'
    })
}


export const getLeaveList = (params) => {
    return request({
        url: '/leave/list', method: 'GET', params: params
    })
}


export const getLeavePage = (params) => {
    return request({
        url: '/leave/page', method: 'GET', params: params
    })
}


export const getLeaveOne = (params) => {
    return request({
        url: '/leave', method: 'GET', params: params
    })
}
