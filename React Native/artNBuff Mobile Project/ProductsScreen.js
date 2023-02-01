import React, {useCallback, useEffect, useState, useRef} from 'react';
import {
    Button,
    StyleSheet,
    SafeAreaView,
    FlatList,
    Text,
    View,
    Image,
    TouchableOpacity,
    Linking,
} from 'react-native';

import currencyConverter from '../../currencyConverter';
import {SelectList} from 'react-native-dropdown-select-list';

import AsyncStorage from '@react-native-async-storage/async-storage';
;

const ProductsScreen = ({route, navigation}) => {
    const [exchangeRate, setExchangeRate] = useState(0);

    const defaultParams = {
        apiURL: 'https://stageanbshopin.wpengine.com/wp-json/wcfmmp/v1/products?per_page=15&page=1',
        defaultURL: true,
    };

    const [apiURL, setApiURL] = useState(() => {
        if (route.params) {
            return route.params.apiURL;
        }
        return defaultParams.apiURL;
    });
    const [defaultURL, setDefaultURL] = useState(defaultParams.defaultURL);

    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [selected, setSelected] = useState('INR');

    const pageNumber = useRef(1);
    const nonceToken = useRef();

    let currentType = [
        {key: '1', value: 'INR'},
        {key: '2', value: 'USD'},
        {key: '3', value: 'EUR'},
    ];
    const updateURLWithPageNumber = apiEndPoint => {
        setApiURL(apiEndPoint.slice(0, -7) + '&page=' + pageNumber.current);
    };

    const endReached = useCallback(() => {
        pageNumber.current += 1;
        updateURLWithPageNumber(apiURL);
    }, [apiURL]);

    useEffect(() => {
        fetch(apiURL)
            .then(response =>
                response.status === 200 ? response.json() : null,
            )
            .then(jsonData => {
                if (jsonData) {
                    setData(currentData => currentData.concat(jsonData));
                } else {
                    endReached();
                }
                setIsLoading(false);
            })
            .catch(error => {
                console.log(error);
            });
    }, [apiURL, endReached]);

    useEffect(() => {
        if (route.params) {
            setData([]);
            pageNumber.current = 1;
            updateURLWithPageNumber(route.params.apiURL);
            setDefaultURL(route.params.defaultURL);
        }
        route.params = false;
    }, [route]);

    const removeSearchOnPress = () => {
        setData([]);
        pageNumber.current = 1;
        setApiURL(defaultParams.apiURL);
        setDefaultURL(defaultParams.defaultURL);
    };

    const onPressButton = props => {
        if (nonceToken.current == null) {
            fetchToken();
        } else {
            addToCartReq();
        }

        async function fetchToken() {
            let response = await fetch(
                'https://stageanbshopin.wpengine.com/wp-json/wc/store/v1/cart',
            );
            nonceToken.current = response.headers.get('Nonce');
            const storeToken = async () => {
                try {
                    await AsyncStorage.setItem('Token', nonceToken.current);
                } catch (error) {
                    console.log(error);
                }
            };
            storeToken();
            addToCartReq();
        }

        async function addToCartReq() {
            try {
                fetch(
                    'https://stageanbshopin.wpengine.com/wp-json/wc/store/v1/cart/add-item?id=' +
                        props +
                        '&quantity=1',
                    {
                        method: 'POST',
                        headers: {
                            Nonce: nonceToken.current,
                        },
                    },
                );
            } catch (error) {
                console.log(error);
            }
        }
    };

    const retrieveRate = async () => {
        try {
            currencyConverter(selected);
            setExchangeRate(await AsyncStorage.getItem('Exchange'));
//            console.log(
//                'Exchange Rate for INR to ' +
//                    selected +
//                    ' in Product: ' +
//                    exchangeRate,
//            );
        } catch (error) {
            console.log(error);
        }
    };

    const formatter = props => {
        if (selected === 'INR') {
            return Intl.NumberFormat('en-IN', {
                style: 'currency',
                currency: 'INR',
            }).format(props);
        } else if (selected === 'USD') {
            try {

                const updatedAmount = props * exchangeRate;
                return Intl.NumberFormat('en-US', {
                    style: 'currency',
                    currency: 'USD',
                }).format(updatedAmount);
            } catch (err) {
                setTimeout(() => 800);
                console.log('Error caught');
                return;
            }
        } else if (selected === 'EUR') {
            try {
                const updatedAmount = props * exchangeRate;
                return Intl.NumberFormat('de-DE', {
                    style: 'currency',
                    currency: 'EUR',
                }).format(updatedAmount);
            } catch (err) {
                setTimeout(() => 800);
                console.log('Error caught');
                return;
            }
        }
    };

    return (
        <SafeAreaView>
            {!isLoading && !defaultURL && (
                <Button onPress={removeSearchOnPress} title="Remove Filter" />
            )}
            {!isLoading && data && (
                <View>
                    <SelectList
                        setSelected={val => setSelected(val)}
                        data={currentType}
                        save="value"
                        label="Preferred Currency"
                        placeholder={selected}
                        onSelect={() => retrieveRate()}
                    />
                    <FlatList
                        numColumns={2}
                        keyExtractor={item => item.id}
                        data={data.slice(1)}
                        renderItem={({item}) => (
                            <View style={styles.itemView}>
                                <Image
                                    style={styles.productImage}
                                    source={{uri: item.images[0].src}}
                                />
                                <Text numberOfLines={1} style={styles.title}>
                                    {item.name}
                                </Text>
                                <Text style={styles.info}>
                                    {formatter(item.price)}
                                </Text>
                                <Text style={styles.rating}>Rating 4/5</Text>
                                <View style={styles.storeInfo}>
                                    <Image
                                        style={styles.logo}
                                        source={{uri: item.store.vendor_banner}}
                                    />
                                    <Text
                                        style={styles.hyperlinkStyle}
                                        onPress={() => {
                                            Linking.openURL(
                                                'https://stageanbshopin.wpengine.com/by/thearticraftfactory/',
                                            );
                                        }}>
                                        {item.store.vendor_display_name}
                                    </Text>
                                </View>
                                <TouchableOpacity
                                    style={styles.addToCartTouchable}
                                    onPress={() => onPressButton(item.id)}>
                                    <Text>Add to Cart</Text>
                                </TouchableOpacity>
                            </View>
                        )}
                        onEndReachedThreshold={2}
                        onEndReached={endReached}
                    />
                </View>
            )}
            {isLoading && <Text>Loading...</Text>}
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        marginTop: 50,
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
    addToCartTouchable: {
        height: 30,
        marginRight: 40,
        marginLeft: 40,
        marginTop: 5,
        marginBottom: 10,
        backgroundColor: 'turquoise',
        alignItems: 'center',
        justifyContent: 'center',
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

export default ProductsScreen;
