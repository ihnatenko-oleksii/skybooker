import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';
import { flightApi } from '../api/apiClient';

function FlightDetailsPage() {
    const { t } = useTranslation();
    const { id } = useParams();
    const navigate = useNavigate();
    const location = useLocation();
    const { isAuthenticated } = useAuth();

    // Get passengers from query param if available
    const queryParams = new URLSearchParams(location.search);
    const initialPassengers = parseInt(queryParams.get('passengers')) || 1;

    const [flight, setFlight] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [passengers, setPassengers] = useState(initialPassengers);

    useEffect(() => {
        loadFlight();
    }, [id]);

    const loadFlight = async () => {
        try {
            const response = await flightApi.getFlightById(id);
            setFlight(response.data);
        } catch (err) {
            setError(err.response?.data?.message || t('errors.loadFlight'));
        } finally {
            setLoading(false);
        }
    };

    const handleReserve = () => {
        if (!isAuthenticated) {
            // Redirect to login with the booking form URL as redirect parameter
            const bookingUrl = `/bookings/new?flightId=${id}&passengers=${passengers}`;
            navigate(`/login?redirect=${encodeURIComponent(bookingUrl)}`);
            return;
        }
        navigate(`/bookings/new?flightId=${id}&passengers=${passengers}`);
    };

    const updatePassengers = (change) => {
        setPassengers(prev => Math.max(1, Math.min(flight?.availableSeats || 10, prev + change)));
    };

    const formatDateTime = (dateTime) => {
        return new Date(dateTime).toLocaleString([], {
            weekday: 'long',
            day: 'numeric',
            month: 'long',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    if (loading) return <div className="loading">{t('common.loading')}</div>;
    if (error) return <div className="error">{error}</div>;
    if (!flight) return <div className="error">{t('errors.notFound')}</div>;

    return (
        <div className="details-container">
            <h1 className="details-title">{t('flightDetails.title')} {flight.flightNumber}</h1>

            <div className="details-grid">
                <div className="details-main card">
                    <div className="route-header">
                        <div className="airport">
                            <span className="code">{flight.fromAirport}</span>
                            <span className="label">{t('search.from')}</span>
                        </div>
                        <div className="arrow">→</div>
                        <div className="airport">
                            <span className="code">{flight.toAirport}</span>
                            <span className="label">{t('search.to')}</span>
                        </div>
                    </div>

                    <div className="flight-info-grid">
                        <div className="info-item">
                            <span className="info-label">{t('flightDetails.departure')}</span>
                            <span className="info-value">{formatDateTime(flight.departureAt)}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">{t('flightDetails.arrival')}</span>
                            <span className="info-value">{formatDateTime(flight.arrivalAt)}</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">{t('flightDetails.duration')}</span>
                            <span className="info-value">{flight.durationMinutes} min</span>
                        </div>
                        <div className="info-item">
                            <span className="info-label">{t('flightDetails.class')}</span>
                            <span className="info-value">{flight.travelClass}</span>
                        </div>
                    </div>

                    <div className="details-extra">
                        <h3>{t('flightDetails.baggage')}</h3>
                        <p>{t('flightDetails.baggageInfo')}</p>

                        <h3 style={{ marginTop: '1.5rem' }}>{t('flightDetails.rules')}</h3>
                        <p>{t('flightDetails.rulesInfo')}</p>
                    </div>
                </div>

                <div className="details-sidebar card">
                    <div className="price-tag">
                        <span className="price-label">{t('flightDetails.price')}</span>
                        <span className="price-amount">{flight.price.toFixed(2)} {flight.currency}</span>
                        <span className="per-person">{t('bookingForm.pricePerPerson')}</span>
                    </div>

                    <div className="passengers-box">
                        <label>{t('flightDetails.passengers')}</label>
                        <div className="passengers-selector">
                            <button type="button" onClick={() => updatePassengers(-1)} disabled={passengers <= 1}>−</button>
                            <span>{passengers}</span>
                            <button type="button" onClick={() => updatePassengers(1)} disabled={passengers >= flight.availableSeats}>+</button>
                        </div>
                        <p className="seats-left">{flight.availableSeats} {t('flightDetails.seats')}</p>
                    </div>

                    <div className="total-box">
                        <div className="total-row">
                            <span>{t('bookingForm.totalPrice')}</span>
                            <strong>{(flight.price * passengers).toFixed(2)} {flight.currency}</strong>
                        </div>
                        <button
                            onClick={handleReserve}
                            className="btn-primary"
                            disabled={passengers < 1 || passengers > flight.availableSeats}>
                            {t('flightDetails.bookButton')}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default FlightDetailsPage;
