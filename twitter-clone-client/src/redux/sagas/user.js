import {call, put, takeLatest} from 'redux-saga/effects';
import userActions from '../actions/user';
import UserService from '../services/user.service';

function* login(action) {
    yield put({type: userActions.LOGIN_REQUEST});
    try {
        const data = yield call(UserService.signin, {
            identifier: action.identifier,
            password: action.password
        });

        yield put({type: userActions.LOGIN_SUCCESS, data: {token: data.jwt, ...data.user}});
    } catch (error) {
        let errorMessage = 'Error when logging in, please try again later';
        if (error && error.message) {
            errorMessage = error.message
        }

        yield put({type: userActions.LOGIN_FAIL, error: errorMessage});
    }
}

function* register(action) {
    yield put({type: userActions.REGISTER_REQUEST});
    try {
        const data = yield call(UserService.signup, {
            username: action.username,
            email: action.email,
            password: action.password
        });

        yield put({type: userActions.REGISTER_SUCCESS, token: data.jwt, ...data.user});
    } catch (error) {
        let errorMessage = 'Error when registering an profile, please try again later';
        if (error && error.message) {
            errorMessage = error.message
        }

        yield put({type: userActions.REGISTER_FAIL, error: errorMessage});
    }
}

function* logout() {
    yield put({type: userActions.LOGOUT_REQUEST});
}

function* forgotPassword(action) {
    try {
        const data = yield call(UserService.forgotPassword, {
            email: action.email
        });

        const ok = data.ok;

        if (ok) {
            yield put({type: userActions.FORGOT_PASSWORD_SUCCESS});
        }
    } catch (error) {
        let errorMessage = 'requesting-recover-error';
        if (error && error.message) {
            errorMessage = error.message
        }

        yield put({type: userActions.FORGOT_PASSWORD_FAIL, error: errorMessage});
    }
}

function* resetPassword(action) {
    try {
        const data = yield call(UserService.resetPassword, {
            code: action.code,
            password: action.password,
            passwordConfirmation: action.passwordConfirmation
        });

        yield put({type: userActions.LOGIN_SUCCESS, token: data.jwt, ...data.user});
    } catch (error) {
        let errorMessage = 'reset-password-error';
        if (error && error.message) {
            errorMessage = error.message
        }
    }
}

function* updateSelf(action) {
    try {
        const data = yield call(UserService.update, {
            ...action.user
        })
        yield put({ type: userActions.UPDATE_SUCCESS, data })
    } catch(error) {
        let errorMessage = 'reset-password-error';
        if (error && error.message) {
            errorMessage = error.message
        }
    }
}

export default [
    takeLatest(userActions.LOGIN, login),
    takeLatest(userActions.REGISTER, register),
    takeLatest(userActions.LOGOUT, logout),
    takeLatest(userActions.FORGOT_PASSWORD, forgotPassword),
    takeLatest(userActions.RESET_PASSWORD, resetPassword),
    takeLatest(userActions.UPDATE, updateSelf)
];
