import React from 'react';
import { StyleSheet, View, Dimensions, Platform } from 'react-native';
import { Avatar, Icon, Button, ListItem, Text, ThemeProvider } from 'react-native-elements';
import Colors from '../../constants/Colors';

const isLandscape = () => Dimensions.get('window').width > Dimensions.get('window').height;

const FeatureItem = (props) => (
    <ListItem
        title={props.title}
        subtitle={props.subtitle}
        containerStyle={{ backgroundColor: 'transparent', padding: 5 }}
        titleStyle={[styles.h2Style, { fontSize: 24 }]}
        subtitleStyle={[styles.h3Style, { marginTop: -2 }]}
        leftAvatar={
            <Avatar
                size="large"
                icon={{ name: props.name, type: 'font-awesome' }}
                overlayContainerStyle={{ backgroundColor: 'transparent' }}
            />
        }
    />
);

const RoundButton = (props) => <Button {...props} buttonStyle={[{ borderRadius: 20 }, props.buttonStyle]} />

export default class WelcomeScreen extends React.PureComponent {
    static navigationOptions = {
        header: null,
    };

    _goToSignIn = () => {
        this.props.navigation.navigate('SignIn');
    };

    _goToSignUp = () => {
        this.props.navigation.navigate('SignUp');
    };

    renderFooter() {
        return (
            <View><Text>Footer</Text></View>
        );
    }

    renderBluePane() {
        return (
            <View style={[styles.wrapper, styles.bluePane]}>
                <View style={[styles.wrapper, { padding: 60, alignItems: 'stretch' }]}>
                    <Text h2 h2Style={styles.h2Style}>You're almost there with the shiny TwitterClone</Text>
                    <Text h3 h3Style={styles.h3Style}>We are in development of the following features:</Text>
                    <FeatureItem name="search" title="Explore" subtitle="Get the trends, news, videos in one place." />
                    <FeatureItem name="bookmark" title="Bookmark" subtitle="Save content to enjoy later." />
                    <FeatureItem name="edit" title="Personalize" subtitle="Costomize you experience." />
                </View>
            </View>
        );
    }

    renderOptionPane() {
        return (
            <View style={[styles.wrapper, styles.optionPane]}>
                <View style={{ padding: '20%' }}>
                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginVertical: 5 }}>
                        <Icon
                            name='heartbeat'
                            type='font-awesome'
                            color={Colors.primaryColor}
                            size={40} />
                        <RoundButton
                            title="Log in"
                            type="outline"
                            onPress={this._goToSignIn}
                        />
                    </View>
                    <Text h2 h2Style={styles.optionPaneTitle}>See what's going on in the world now!</Text>
                    <Text h3 h3Style={[styles.optionPaneTitle, { fontSize: 18, marginTop: 50 }]}>Join TwitterClone today.</Text>
                    <RoundButton
                        title="Sign up"
                        buttonStyle={{ marginVertical: 20, backgroundColor: Colors.primaryColor }}
                        onPress={this._goToSignUp}
                    />
                    <RoundButton
                        title="Log in"
                        type="outline"
                        onPress={this._goToSignIn}
                    />
                </View>
            </View>
        );
    }

    render() {
        return (
            <ThemeProvider>
                <View style={styles.container}>
                    {Platform.OS == "web" && this.renderBluePane()}
                    {this.renderOptionPane()}
                </View>
                {Platform.OS == "web" && this.renderFooter()}
            </ThemeProvider>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        flexDirection: isLandscape() ? 'row' : 'column-reverse',
        justifyContent: 'space-evenly',
        alignItems: 'stretch',
        backgroundColor: '#fff',
    },
    wrapper: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    bluePane: {
        flex: 1,
        backgroundColor: Colors.primaryColor,
    },
    h2Style: {
        color: 'white',
        fontWeight: 'bold',
    },
    h3Style: {
        color: 'white',
        marginTop: 10
    },
    optionPane: {
        flex: 1,
        alignItems: 'stretch',
    },
    optionPaneTitle: {
        fontSize: 30,
        fontWeight: 'bold'
    }
});