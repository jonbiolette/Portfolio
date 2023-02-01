import React, {useEffect, useState} from 'react';
import {
    Text,
    View,
    FlatList,
    Button,
    StyleSheet,
    SafeAreaView,
} from 'react-native';

import AsyncStorage from '@react-native-async-storage/async-storage';

const CartScreen = ({route, navigation}) => {
    const [tokenFound, setTokenStatus] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    const [tokenID, setTokenID] = useState(0);
    const [productArray, setProductArray] = useState([]);

    const retrieveToken = async () => {
        try {
            const receivedTokenID = await AsyncStorage.getItem('Token');
            if (receivedTokenID !== null) {
                console.log('nonce token found: ' + receivedTokenID);
                setTokenStatus(true);
                setTokenID(receivedTokenID);
            }
        } catch (error) {
            console.log(error);
        }
    };

    useEffect(() => {
        retrieveToken();

        fetch('https://stageanbshopin.wpengine.com/wp-json/wc/store/v1/cart', {
            method: 'GET',
            headers: {
                Nonce: tokenID,
            },
        })
            .then(response => response.json())
            .then(data => {
                const cartItems = [];
                data.items.forEach(item => {
                    const product = {
                        id: item.id,
                        name: item.name,
                        image: item.images[0].src,
                        price: item.prices.price,
                    };
                    console.log("Item id: "+ item.id)
                    console.log("Item name: "+ item.name)
                    cartItems.push(product);
                });

                setProductArray(cartItems);

            })
            .catch(err => console.error(err))
            .finally(() => {

                setIsLoading(false);
            });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const Item = ({item}) => (
        <View>
            <Text>{item.name}</Text>
        </View>
    );

    const renderProduct = ({item}) => <Item item={item} />;

    return (
        <SafeAreaView>
            {/* If token is not found means cart has not been started
            user re directed back to products page */}
            {!tokenFound && (
                <View style={styles.middleContainer}>
                    <Text>Your cart is empty</Text>
                    <Button
                        onPress={() => navigation.navigate('Products')}
                        title="Continue Shopping"
                        color="#2196F3"
                    />
                </View>
            )}
            {/* ------ If token is, loading products */}
            {tokenFound && isLoading && (
                <View>
                    <Text>Loading</Text>
                </View>
            )}
            {/* ------ If token is found and loading is finished */}
            {tokenFound && !isLoading && (
                <View>
                    <Text>Token Found</Text>
                    <Text>Products currently in Cart:</Text>

                    <FlatList
                        data={productArray}
                        renderItem={renderProduct}
                        keyExtractor={item => item.id}
                    />
                </View>
            )}
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    middleContainer: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    itemView: {
        flex: 0.5,
    },
    productImage: {
        resizeMode: 'cover',
        width: 185,
        height: 185,
        margin: 5,
    },
    title: {
        fontSize: 15,
        fontWeight: 'bold',
        textAlign: 'center',
    },
    info: {
        fontSize: 12,
        textAlign: 'center',
    },
    storeInfo: {
        flexDirection: 'row',
        height: 40,
        padding: 10,
    },
    logo: {
        flex: 0.25,
    },
    hyperlinkStyle: {
        flex: 0.75,
        fontSize: 14,
        color: 'blue',
        textAlign: 'center',
        justifyContent: 'center',
    },
    rating: {
        fontSize: 12,
        textAlign: 'center',
    },
    price: {
        fontSize: 12,
        textAlign: 'center',
    },
});

export default CartScreen;
