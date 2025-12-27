import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { bookingApi } from '../api/apiClient';

function BookingsListPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadBookings();
    }, []);

    const loadBookings = async () => {
        try {
            const response = await bookingApi.getMyBookings();
            setBookings(response.data);
        } catch (err) {
            setError(err.response?.data?.message || t('errors.loadBookings'));
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = async (bookingId) => {
        if (!window.confirm(t('bookingsList.cancelConfirm'))) {
            return;
        }

        try {
            await bookingApi.cancelBooking(bookingId);
            loadBookings();
        } catch (err) {
            alert(err.response?.data?.message || t('errors.cancelBooking'));
        }
    };

    if (loading) return <div className="loading">{t('common.loading')}</div>;
    if (error) return <div className="container"><div className="error">{error}</div></div>;

    return (
        <div className="bookings-page-container">
            <h1 className="section-title">{t('bookingsList.title')}</h1>

            {bookings.length === 0 ? (
                <div className="empty-state card">
                    <div className="empty-icon">✈</div>
                    <h2>{t('bookingsList.noBookings')}</h2>
                    <p>{t('bookingsList.startSearching')}</p>
                    <button onClick={() => navigate('/')} className="btn-primary" style={{ marginTop: '1.5rem' }}>
                        {t('search.searchButton')}
                    </button>
                </div>
            ) : (
                <div className="bookings-list">
                    {bookings.map((booking) => (
                        <div key={booking.id} className="booking-card card">
                            <div className="booking-header">
                                <div className="id-group">
                                    <span className="label">{t('payment.reservationId')}</span>
                                    <span className="value">#{booking.id}</span>
                                </div>
                                <span className={`status-badge ${booking.status.toLowerCase()}`}>
                                    {t(`bookingsList.status.${booking.status}`)}
                                </span>
                            </div>

                            <div className="booking-body">
                                <div className="route-info">
                                    <div className="flight-num">{booking.flight.flightNumber}</div>
                                    <div className="route-path">
                                        <strong>{booking.flight.fromAirport}</strong>
                                        <span className="dots">•••••</span>
                                        <strong>{booking.flight.toAirport}</strong>
                                    </div>
                                    <div className="departure-time">
                                        {new Date(booking.flight.departureAt).toLocaleString()}
                                    </div>
                                </div>

                                <div className="booking-stats">
                                    <div className="stat">
                                        <span className="label">{t('payment.passengers')}</span>
                                        <span className="value">{booking.passengers.length}</span>
                                    </div>
                                    <div className="stat">
                                        <span className="label">{t('flightDetails.price')}</span>
                                        <span className="value">{booking.totalPrice.toFixed(2)} {booking.currency}</span>
                                    </div>
                                </div>

                                <div className="booking-actions">
                                    {(booking.status === 'PENDING_PAYMENT' || booking.status === 'PAYMENT_FAILED') && (
                                        <button
                                            onClick={() => navigate(`/payment?bookingId=${booking.id}`)}
                                            className="btn-primary btn-pay">
                                            {t('bookingsList.payNow')}
                                        </button>
                                    )}

                                    {booking.status !== 'CANCELLED' && (
                                        <button
                                            onClick={() => handleCancel(booking.id)}
                                            className="btn-text-danger">
                                            {t('bookingsList.cancel')}
                                        </button>
                                    )}
                                </div>
                            </div>

                            <details className="passenger-details">
                                <summary>{t('bookingsList.passengerDetails')}</summary>
                                <div className="details-content">
                                    {booking.passengers.map((passenger, index) => (
                                        <div key={passenger.id} className="passenger-row">
                                            <strong>{passenger.firstName} {passenger.lastName}</strong>
                                            <span>{passenger.birthDate} | {passenger.documentNumber}</span>
                                        </div>
                                    ))}
                                </div>
                            </details>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default BookingsListPage;
