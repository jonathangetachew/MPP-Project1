import React, { Component } from 'react';
import {
    View,
    StyleSheet,
    FlatList,
    Image,
    ActivityIndicator,
    TouchableOpacity
} from 'react-native';
import { connect } from 'react-redux';
import constants from '../redux/actions/tweets';
import Colors from '../constants/Colors';
import { Text, Icon, Button } from "react-native-elements";
class TweetList extends Component {
    constructor() {
        super();
        this.state = {
            dataSource: [],
            isLoading: true
        }
    }
    componentDidMount() {
        this.props.getTweets();
    }

    renderItem = ({ item }) => {
        return (
            <TouchableOpacity
                style={{
                    flex: 1,
                    justifyContent: 'center',
                    flexDirection: 'row',
                    marginBottom: 3
                }}
            >
                <Image style={styles.profileimage}
                    source={{ uri: item.picture }}
                />
                <View style={{ flex: 1 }}>
                    <Text style={{ fontSize: 15, width: 900, color: '#000000' }}>
                        {item.name} <Text style={{ fontSize: 15, color: '#777' }}>@{item.username}</Text>
                    </Text>
                    <Text style={{}}>
                        {item.body}
                    </Text>
                    <View style={{ flex: 1, flexDirection: "row", justifyContent: "space-around" }}>
                        <View style={styles.iconView}>
                            <Icon
                                name='comment'
                                type='font-awesome'
                                color='#657786'
                                size="18"
                            />
                            <Text style={styles.subtext}>1</Text>
                        </View>
                        <View style={styles.iconView}>
                            <Icon
                                name='retweet'
                                type='font-awesome'
                                color='#657786'
                                size="18"
                            />
                            <Text style={styles.subtext}>1</Text>
                        </View>
                        <View style={styles.iconView}>
                            <Icon
                                name='heart'
                                type='font-awesome'
                                color='#657786'
                                size="20"
                            />
                            <Text style={styles.subtext}>1</Text>
                        </View>
                     </View>
                </View>
            </TouchableOpacity>
        );
    }

    renderFixedHeader = () => {
        return (
            <View style={styles.header_style}>
                <Text h2 h2Style={{ marginTop: 10, marginBottom: 3, paddingBottom: 3, fontWeight: 700, fontSize: 22, paddingLeft: 5 }}>Latest Tweets</Text>
                <View style={{ width: '100%', height: 1, backgroundColor: '#ccc' }} />
            </View>
        );
    }
    renderSeperator = () => {
        return (
            <View style={{ width: '100%', height: 1, backgroundColor: '#ccc', marginVertical: 5 }} />
        );
    }

    handleLoadMore = () => {
        warn.log("Warn load more");
    }
    render() {
        const { tweetData, loading, error } = this.props;
        if (loading) return <ActivityIndicator />
        return (
            <View style={styles.container}>
                <FlatList
                    data={tweetData}
                    renderItem={this.renderItem}
                    keyExtractor={(item, id) => id.toString()}
                    ItemSeparatorComponent={this.renderSeperator}
                    ListHeaderComponent={this.renderFixedHeader}
                    stickyHeaderIndices={[0]}
                    onEndReached={this.handleLoadMore}
                    onEndReachedThreshold={0}
                />
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: 'row',
        backgroundColor: '#FFF',
        maxWidth: 600,
        borderLeftWidth: 1,
        borderLeftColor: '#e6ecf0',
        borderRightWidth: 1,
        borderRightColor: '#e6ecf0',
    },
    profileimage: {
        width: 50,
        height: 50,
        borderRadius: 25,
        marginHorizontal: 5,
        marginTop: 5,
        overflow: "hidden",
    },
    infocontainer: {
        flex: 1,
        justifyContent: 'flex-start'
    },
    header: {
        marginBottom: 15,
        paddingBottom: 15
    },
    header_style: {
        borderBottomWidth: 1,
        borderBottomColor: '#CCC',
        marginLeft: 2
    },
    subtext: {
        marginRight: 10,
        color: '#657786',
        fontSize: 15,
        position: "absolute",
        top: -2,
        left: 20
    },
    iconView: {
        marginLeft: 1,
        flex: 1,
        flexDirection: "row"
    }
});

export default connect(({ tweets }) => ({
    tweetData: Object.values(tweets.data),
    loading: tweets.loading
}), dispatch => ({
    getTweets: () => {
        dispatch({ type: constants.GET_TWEETS });
    }
}))(TweetList);