import { call, put, takeLatest } from 'redux-saga/effects';
import constants from '../actions/tweets';
import { store } from '../store';
import tweetServiceMock from '../services/tweet.service.mock';

function* items(action) {
    yield put({ type: constants.GET_TWEETS_REQUEST });
    try {
        const data = yield call(tweetServiceMock.getTweets);
       
        yield put({ type: constants.GET_TWEETS_SUCCESS, data: data.map((item, key) => !item.id ? { ...item, id: key } : item) });
    } catch (error) {
        let errorMessage = 'Error when retrieving items';
        if (error && error.message) {
            errorMessage = error.message;
        }

        yield put({ type: constants.GET_TWEETS_FAIL, error: errorMessage });
    }
}

function* searchItems(action) {
    yield put({ type: constants.SEARCH_TWEETS_REQUEST });

    try {
        const data = yield call(tweetServiceMock.search, action.criteria);        
        yield put({ type: constants.SEARCH_TWEETS_SUCCESS, data: data.map((item, key) => !item.id ? { ...item, id: key } : item) });
    } catch (error) {

        let errorMessage = 'Error when retrieving items';
        if (error.message.includes("504")) {
            errorMessage = "Error with the criteria. Must be alphanum."
        } else if (error && error.message) {
            errorMessage = error.message;
        }

        yield put({ type: constants.SEARCH_TWEETS_FAIL, error: errorMessage });
    }
}

export default [
    takeLatest(constants.GET_TWEETS, items),
    takeLatest(constants.SEARCH_TWEETS, searchItems),
];
