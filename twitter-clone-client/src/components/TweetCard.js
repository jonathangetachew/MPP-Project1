import React from 'react';
import { TouchableOpacity, View, StyleSheet } from 'react-native';
import { Text, Icon, Avatar } from 'react-native-elements';

export default (props) => {
    const { picture, name, username, body } = props;
    return (
        <TouchableOpacity
            style={{
                flex: 1,
                justifyContent: 'center',
                alignItems: 'center',
                flexDirection: 'row',
                marginBottom: 3
            }}
        >
            <Avatar
                style={styles.profileimage}
                source={{ uri: picture }}
            />
            <View style={{ flex: 1 }}>
                <Text style={{ fontSize: 15, width: 900, color: '#000000' }}>
                    {name} <Text style={{ fontSize: 15, color: '#777' }}>@{username}</Text>
                </Text>
                <Text style={{}}>
                    {body}
                </Text>
                <View style={{ flex: 1, flexDirection: "row", justifyContent: "space-around" }}>
                    <View style={styles.iconView}>
                        <Icon
                            name='comment'
                            type='font-awesome'
                            color='#657786'
                            size={18}
                        />
                        <Text style={styles.subtext}>1</Text>
                    </View>
                    <View style={styles.iconView}>
                        <Icon
                            name='retweet'
                            type='font-awesome'
                            color='#657786'
                            size={18}
                        />
                        <Text style={styles.subtext}>1</Text>
                    </View>
                    <View style={styles.iconView}>
                        <Icon
                            name='heart'
                            type='font-awesome'
                            color='#657786'
                            size={18}
                        />
                        <Text style={styles.subtext}>1</Text>
                    </View>
                </View>
            </View>
        </TouchableOpacity>
    );
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