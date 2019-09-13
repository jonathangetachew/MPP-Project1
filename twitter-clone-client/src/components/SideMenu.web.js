import PropTypes from 'prop-types';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavigationActions, DrawerItems, createDrawerNavigator } from 'react-navigation';
import { StyleSheet, StatusBar, ScrollView, View } from 'react-native';
import { Avatar, Icon, Text } from 'react-native-elements';
import Colors from '../constants/Colors';
import sidemenuDescriptor from '../navigation/sidemenu-descriptor';

/**
 * Code idea obtained from https://codeburst.io/custom-drawer-using-react-navigation-80abbab489f7
 */
class SideMenu extends Component {
    state = {
        expanded: true
    };

    constructor() {
        super();

        this._onToggleExpand = this._onToggleExpand.bind(this);
    }
    navigateToScreen = (route) => () => {
        const navigateAction = NavigationActions.navigate({
            routeName: route
        });
        this.props.navigation.dispatch(navigateAction);
    }

    _onToggleExpand() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    renderOtherOptions() {
        return null;
    }

    renderMenuOptions() {
        console.log("Cosa",sidemenuDescriptor)
        return (
            <ScrollView>
                {/*asd*/}
            </ScrollView>
        );
    }

    render() {
        const { expanded } = this.state;
        //console.log("USER_TOKEN", this.props.user);
        return (
            <View style={styles.container}>
                {expanded ? this.renderMenuOptions() : this.renderOtherOptions()}
            </View>
        );
    }
}

SideMenu.propTypes = {
    navigation: PropTypes.object
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'stretch',
        backgroundColor:'blue'
    },
    userInfo: {
        padding: 20,
        marginTop: StatusBar.currentHeight,
        borderBottomColor: '#ececec',
        borderBottomWidth: 1
    },
    textNameStyle: {
        fontSize: 18,
        fontWeight: 'bold'
    },
    numericBold: {
        fontWeight: 'bold',
        marginEnd: 5
    }
});

export default SideMenu;