import axios from 'axios'
//import Router from "../../router"
var config = require('../../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({

	baseURL: backendUrl,
	headers: {
		'Access-Control-Allow-Origin': frontendUrl
	}
}) 

export default {
	data() {
		return {
			restaurants: [],
			restaurantError: '',
			response: [],
			favoriteRestaurants: [],
			favoriteRestaurantIDs: [],
			customer: '',
			filterType: '',
		}
	},

	created: function() {
		this.customer = this.$store.state.user;
		this.getFavorites()
		this.getAllRestaurants()
	},
	methods: {
		getRestoPage: function(restoID) {
			AXIOS.get('/getRestaurant/ID/?ID='.concat(restoID))
				.then(response => {
					this.$store.state.restaurant = response.data
				})
				.catch(e => {
					this.restaurantError = e
				})
			return '/#/restaurantInfo/'

		},
		searchRestaurants: function(inputString, price, minPrice, maxPrice, filterType) {
			if (filterType == 'Restaurant Name') {
				this.getRestaurantsByName(inputString)
			} else if (filterType == 'Price') {
				this.getRestaurantsByPrice(price)
			} else if (filterType == 'Price Range') {
				this.getRestaurantsByPriceRange(minPrice, maxPrice)
			} else if (filterType == 'Cuisine') {
				this.getRestaurantsByCuisine(inputString)
			} else if (filterType == 'Food Options') {
				this.getRestaurantsByOptions(inputString)
			} else if (filterType == 'All Restaurants') {
				this.getAllRestaurants()
			} else {
				alert("Please choose a filter")
			}
		}, getAllRestaurants: function() {
			AXIOS.get('/restaurants')
			.then(response => {
				this.restaurants = response.data
				this.restaurants.forEach(resto => {
					this.favoriteRestaurants.forEach(faveResto => {
						if(faveResto.id == resto.id) {
							resto.isFavorite = true
							return
						}
					})
					resto.isFavorite = false
				})
			})
			.catch(e => {
				this.restaurantError = e
			})
		},
		getRestaurantsByName: function(nameFragment) {
			AXIOS.get('/restaurantsByNameContains?nameFragment='.concat(nameFragment), {}, {params: {nameFragment: nameFragment}})
			.then(response => {
				this.restaurants = response.data
			}).catch(e => {
				this.restaurantError = e
			})
		},
		getRestaurantsByPrice: function(price) {
			AXIOS.get('/restaurants/price?price='.concat(price), {}, {params: {price:price}})
			.then(response => {
				this.restaurants = response.data
			}).catch(e => {
				this.restaurantError = e
			})
		},getRestaurantsByPriceRange: function(minPrice, maxPrice) {
			AXIOS.get('/restaurants/price/range?minPrice='.concat(minPrice, "&maxPrice=", maxPrice), {}, {params: {minPrice:minPrice, maxPrice:maxPrice}})
			.then(response => {
				this.restaurants = response.data
			}).catch(e => {
				this.restaurantError = e
			})
		},getRestaurantsByCuisine: function(cuisine) {
			AXIOS.get('/restaurants/cuisine?cuisine='.concat(cuisine), {}, {params: {cuisine:cuisine}})
			.then(response => {
				this.restaurants = response.data
			}).catch(e => {
				this.restaurantError = e
			})
		},getRestaurantsByOptions: function(options) {
			AXIOS.get('/restaurants/options?options='.concat(options), {}, {params: {options:options}})
			.then(response => {
				this.restaurants = response.data
			}).catch(e => {
				this.restaurantError = e
			})
		},addToFavorites: function(restaurant) {
			AXIOS.post('/customer/favorite/add?email='.concat(this.customer.email, '&restoID=', parseInt(restaurant.id)))
			.then(response => {
				alert(restaurant.name.concat(" was added to your favorites!"))
				restaurant.isFavorite = true
				this.favoriteRestaurants.push(response.data)
				this.favoriteRestaurantIDs.push(response.data.id)
			}).catch(e => {
				this.restaurantError = e
			})
		},removeFromFavorites: function(restaurant) {
			AXIOS.post('/customer/favorite/remove?email='.concat(this.customer.email, '&restoID=', parseInt(restaurant.id)))
			.then(response => {
				alert(restaurant.name.concat(" was removed from your favorites"))
				restaurant.isFavorite = false
				this.response.push(response)
			}).catch(e => {
				this.restaurantError = e
			})
		},getFavorites: function() {
			this.favoriteRestaurants = []
			this.favoriteRestaurantIDs = []
			AXIOS.get('/customer/favorites?email='.concat(this.customer.email))
			.then(response => {
				this.favoriteRestaurants = response.data
				this.favoriteRestaurants.forEach(element => {
					this.favoriteRestaurantIDs.push(element.id)
				});
			}).catch(e => {
				this.restaurantError = e
			})
		}
	}
}
