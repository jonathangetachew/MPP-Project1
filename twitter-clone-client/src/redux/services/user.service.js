import axios from './axios-instance';
export default {
    signin: async ({ identifier, password }) => {
        return axios.post('login', { username: identifier, password });
    },
    signup: async (user) => {
        return axios.post('signup', user);
    },
    forgotPassword: async ({ identifier }) => {

    },
    resetPassword: async ({ code, password, passwordConfirmation }) => {

    },
    update: async (user) => {

    }
}