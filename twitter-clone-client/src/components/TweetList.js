import React, { Component } from 'react';
import {
    Text,
    View,
    StyleSheet,
    FlatList,
    Image,
    ActivityIndicator,
    TouchableOpacity,
    ToastAndroid
} from 'react-native';
import { connect } from 'react-redux';
import { GET_TWEETS } from '../redux/actions/tweets';

export default class TweetList extends Component {
    constructor() {
        super();
        this.state = {
            dataSource: [],
            isLoading: true
        }
    }
    componentDidMount() {
        /*const url = 'http://www.json-generator.com/api/json/get/bPdpLegwaG?indent=2';  
        fetch(url)
        .then((response)=>response.json())
        .then((responseJson)=>{
          this.setState({
            dataSource:responseJson,
            isLoading:false
          });
        })
        .catch((error) => {
          console.log(error);
        })*/

        this.props.getTweets();
    }

    renderItem = ({ item }) => {
        return (
            <View
                style={{
                    flex: 1,
                    justifyContent: 'center',
                    flexDirection: 'row',
                    marginBottom: 3,
                }}>
                <Image style={styles.profileimage}
                    source={{ uri: item.picture }}
                />
                <View style={styles.infocontainer}>
                    <Text style={{ fontSize: 15, width: 900, color: '#000000' }}>
                        {item.name} <Text style={{ fontSize: 15, color: '#777' }}>@{item.username}</Text>
                    </Text>
                    <Text style={{}}>
                        {item.body}
                    </Text>
                </View>
            </View>
        );
    }

    renderSeperator = () => {
        return (
            <View style={{ width: '100%', height: 1, backgroundColor: '#ccc', marginVertical: 5 }} />
        );
    }

    render() {
        const { tweetData, loading, error } = this.props;
        if (loading) return <ActivityIndicator />
        
        return (
            <View style={styles.container}>
                <FlatList
                    //data={this.state.dataSource}
                    data={tweetData}
                    renderItem={this.renderItem}
                    keyExtractor={(item, id) => id}
                    ItemSeparatorComponent={this.renderSeperator}
                />
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
        backgroundColor: '#EEE',
        paddingHorizontal: 5
    },
    profileimage: {
        width: 50,
        height: 50,
        borderRadius: 25,
        marginRight: 5,
        marginTop: 5,
        overflow: "hidden",
    },
    infocontainer: {
        flex: 1,
        justifyContent: 'top'
    }
});
export default connect(({ tweets }) => ({
    tweetData: Object.values(tweets.data),
    loading: tweets.loading
}), dispatch => {
    getTweets: () => {
        dispatch({ type: GET_TWEETS });
    }
})(TweetList);