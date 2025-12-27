import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';
import { bookingApi, flightApi } from '../api/apiClient';

function BookingFormPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { isAuthenticated, loading: authLoading } = useAuth();
    const [searchParams] = useSearchParams();
    const flightId = searchParams.get('flightId');
    const passengersCount = parseInt(searchParams.get('passengers')) || 1;

    const [flight, setFlight] = useState(null);
    const [passengers, setPassengers] = useState([]);
    const [extraBaggage, setExtraBaggage] = useState(false);
    const [insurance, setInsurance] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (authLoading) return;

        if (!isAuthenticated) {
            // Redirect to login with current URL as redirect parameter
            const currentUrl = `/bookings/new?flightId=${flightId}&passengers=${passengersCount}`;
            navigate(`/login?redirect=${encodeURIComponent(currentUrl)}`);
            return;
        }

        if (!flightId) {
            setError(t('errors.missingFlightId'));
            return;
        }

        loadFlight();
        initializePassengers();
    }, [flightId, passengersCount, isAuthenticated, authLoading, navigate, t]);

    const loadFlight = async () => {
        try {
            const response = await flightApi.getFlightById(flightId);
            setFlight(response.data);
        } catch (err) {
            setError(t('errors.loadFlight'));
        }
    };

    const initializePassengers = () => {
        const passengerList = [];
        for (let i = 0; i < passengersCount; i++) {
            passengerList.push({
                firstName: '',
                lastName: '',
                birthDate: '',
                documentNumber: ''
            });
        }
        setPassengers(passengerList);
    };

    const handlePassengerChange = (index, field, value) => {
        const updatedPassengers = [...passengers];
        updatedPassengers[index][field] = value;
        setPassengers(updatedPassengers);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const bookingData = {
            flightId: parseInt(flightId),
            passengers: passengers,
            extras: {
                extraBaggage,
                insurance
            }
        };

        try {
            const response = await bookingApi.createBooking(bookingData);
            navigate(`/payment?bookingId=${response.data.id}`);
        } catch (err) {
            setError(err.response?.data?.message || t('errors.createBooking'));
        } finally {
            setLoading(false);
        }
    };

    if (error && !flight) return <div className="container"><div className="error">{error}</div></div>;
    if (!flight) return <div className="loading">{t('common.loading')}</div>;

    return (
        <div className="booking-form-container">
            <h1 className="section-title">{t('bookingForm.title')}</h1>

            <div className="booking-summary-top card">
                <div className="summary-info">
                    <h3>{flight.flightNumber} • {flight.fromAirport} → {flight.toAirport}</h3>
                    <p>{t('bookingForm.pricePerPerson')}: <strong>{flight.price.toFixed(2)} {flight.currency}</strong></p>
                </div>
                <div className="summary-count">
                    <span>{t('flightDetails.passengers')}: <strong>{passengersCount}</strong></span>
                </div>
            </div>

            <form onSubmit={handleSubmit} className="booking-form">
                {passengers.map((passenger, index) => (
                    <div key={index} className="passenger-card card">
                        <div className="card-header">
                            <span className="passenger-number">{t('bookingForm.passenger')} {index + 1}</span>
                        </div>

                        <div className="form-grid">
                            <div className="form-group">
                                <label>{t('bookingForm.name')}</label>
                                <input
                                    type="text"
                                    className="search-input"
                                    value={passenger.firstName}
                                    onChange={(e) => handlePassengerChange(index, 'firstName', e.target.value)}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('bookingForm.surname')}</label>
                                <input
                                    type="text"
                                    className="search-input"
                                    value={passenger.lastName}
                                    onChange={(e) => handlePassengerChange(index, 'lastName', e.target.value)}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('bookingForm.dob')}</label>
                                <input
                                    type="date"
                                    className="search-input"
                                    value={passenger.birthDate}
                                    onChange={(e) => handlePassengerChange(index, 'birthDate', e.target.value)}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label>{t('bookingForm.document')}</label>
                                <input
                                    type="text"
                                    className="search-input"
                                    value={passenger.documentNumber}
                                    onChange={(e) => handlePassengerChange(index, 'documentNumber', e.target.value)}
                                    required
                                />
                            </div>
                        </div>
                    </div>
                ))}

                <div className="extras-card card">
                    <h3 className="card-title">{t('bookingForm.extras')}</h3>

                    <div className="extras-grid">
                        <label className="checkbox-container">
                            <input
                                type="checkbox"
                                checked={extraBaggage}
                                onChange={(e) => setExtraBaggage(e.target.checked)}
                            />
                            <span className="checkmark"></span>
                            <div className="extra-info">
                                <strong>{t('bookingForm.extraBaggage')}</strong>
                                <span>+50.00 {flight.currency}</span>
                            </div>
                        </label>

                        <label className="checkbox-container">
                            <input
                                type="checkbox"
                                checked={insurance}
                                onChange={(e) => setInsurance(e.target.checked)}
                            />
                            <span className="checkmark"></span>
                            <div className="extra-info">
                                <strong>{t('bookingForm.insurance')}</strong>
                                <span>+30.00 {flight.currency}</span>
                            </div>
                        </label>
                    </div>
                </div>

                {error && <div className="error">{error}</div>}

                <div className="final-summary card">
                    <h3 className="card-title">{t('bookingForm.summary')}</h3>
                    <div className="summary-rows">
                        <div className="summary-row">
                            <span>{t('flightDetails.passengers')} ({passengers.length})</span>
                            <span>{(flight.price * passengers.length).toFixed(2)} {flight.currency}</span>
                        </div>
                        {extraBaggage && (
                            <div className="summary-row">
                                <span>{t('bookingForm.extraBaggage')}</span>
                                <span>50.00 {flight.currency}</span>
                            </div>
                        )}
                        {insurance && (
                            <div className="summary-row">
                                <span>{t('bookingForm.insurance')}</span>
                                <span>30.00 {flight.currency}</span>
                            </div>
                        )}
                        <div className="summary-row total">
                            <span>{t('bookingForm.totalPrice')}</span>
                            <strong>{(flight.price * passengers.length + (extraBaggage ? 50 : 0) + (insurance ? 30 : 0)).toFixed(2)} {flight.currency}</strong>
                        </div>
                    </div>

                    <button type="submit" className="btn-primary btn-booking" disabled={loading}>
                        {loading ? t('common.processing') : t('bookingForm.paymentButton')}
                    </button>
                </div>
            </form>
        </div>
    );
}

export default BookingFormPage;
