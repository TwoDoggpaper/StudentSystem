import { instance } from './httpCommon';
import type { AxiosResponse } from 'axios';

interface UserModel {
    username: string;
    password: string;
}

interface RestResponse<T> {
    code?: number;
    message?: string;
    data?: T;
}

// 登录接口（GET 请求）
export function loginApi(data: UserModel): Promise<AxiosResponse<RestResponse<any>>> {
    return instance({
        url: "/user/getByName",
        method: "get",
        params: {
            username: data.username,
            password: data.password
        }
    });
}

// 注册接口（POST JSON）
export function registerApi(data: UserModel): Promise<AxiosResponse<RestResponse<any>>> {
    return instance({
        url: "/user/add",
        method: "post",
        data: data
    });
}