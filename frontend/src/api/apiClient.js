import axios from 'axios';

const apiClient = axios.create({
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

// Add request interceptor to include JWT token in Authorization header
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export const flightApi = {
    searchFlights: (from, to, date, passengers, travelClass) => {
        const params = { from, to, date, passengers };
        if (travelClass) params.travelClass = travelClass;
        return apiClient.get('/flights/search', { params });
    },

    getFlightById: (id) => {
        return apiClient.get(`/flights/${id}`);
    }
};

export const bookingApi = {
    createBooking: (data) => {
        return apiClient.post('/bookings', data);
    },

    getMyBookings: () => {
        return apiClient.get('/bookings/me');
    },

    getBookingById: (id) => {
        return apiClient.get(`/bookings/${id}`);
    },

    cancelBooking: (id) => {
        return apiClient.post(`/bookings/${id}/cancel`);
    }
};

export const paymentApi = {
    processMockPayment: (data) => {
        return apiClient.post('/payments/mock', data);
    }
};

export const airportApi = {
    getAllAirports: () => {
        return apiClient.get('/airports');
    }
};

export default apiClient;
