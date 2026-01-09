import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { bookingApi, paymentApi } from '../api/apiClient';

function PaymentPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const bookingId = searchParams.get('bookingId');

    const [booking, setBooking] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [paymentResult, setPaymentResult] = useState(null);

    useEffect(() => {
        if (!bookingId) {
            setError(t('errors.missingBookingId'));
            return;
        }

        loadBooking();
    }, [bookingId]);

    const loadBooking = async () => {
        try {
            const response = await bookingApi.getBookingById(bookingId);
            setBooking(response.data);
        } catch (err) {
            setError(t('errors.loadBooking'));
        }
    };

    const handlePayment = async (outcome) => {
        setLoading(true);
        setError(null);
        setPaymentResult(null);

        const paymentData = {
            bookingId: parseInt(bookingId),
            method: 'CARD',
            outcome: outcome // SUCCESS or FAIL
        };

        try {
            const response = await paymentApi.processMockPayment(paymentData);
            
            // Po płatności odśwież dane rezerwacji
            await loadBooking();
            
            if (outcome === 'SUCCESS') {
                // Po udanej płatności pokaż komunikat sukcesu
                setPaymentResult({
                    success: true,
                    status: response.data.status,
                    bookingStatus: response.data.bookingStatus
                });
            } else {
                // Po nieudanej płatności wyczyść paymentResult, aby umożliwić ponowną próbę
                // Status zostanie zaktualizowany przez loadBooking()
                setPaymentResult(null);
                setError('Płatność nie powiodła się. Możesz spróbować ponownie.');
                // Po chwili wyczyść błąd, aby nie blokował interfejsu
                setTimeout(() => setError(null), 3000);
            }
        } catch (err) {
            setError(err.response?.data?.message || t('errors.processPayment'));
            // Po błędzie również odśwież dane, aby pokazać aktualny status
            await loadBooking();
            setPaymentResult(null); // Wyczyść paymentResult, aby umożliwić ponowną próbę
        } finally {
            setLoading(false);
        }
    };

    if (error && !booking) return <div className="container"><div className="error">{error}</div></div>;
    if (!booking) return <div className="loading">{t('common.loading')}</div>;

    return (
        <div className="payment-container">
            <h1 className="section-title">{t('payment.title')}</h1>

            {(booking.status === 'PENDING_PAYMENT' || booking.status === 'PAYMENT_FAILED') && !paymentResult?.success && (
                <div className="payment-grid">
                    <div className="payment-summary card">
                        <h2 className="card-title">{t('payment.summary')}</h2>
                        <div className="summary-list">
                            <div className="list-item">
                                <span>{t('payment.reservationId')}:</span>
                                <strong>#{booking.id}</strong>
                            </div>
                            <div className="list-item">
                                <span>{t('bookingForm.flight')}:</span>
                                <strong>{booking.flight.flightNumber}</strong>
                            </div>
                            <div className="list-item">
                                <span>{t('payment.route')}:</span>
                                <strong>{booking.flight.fromAirport} → {booking.flight.toAirport}</strong>
                            </div>
                            <div className="list-item">
                                <span>{t('payment.passengers')}:</span>
                                <strong>{booking.passengers.length}</strong>
                            </div>
                            <div className="list-item">
                                <span>{t('payment.status')}:</span>
                                <span className={`status-badge ${booking.status.toLowerCase()}`}>
                                    {t(`bookingsList.status.${booking.status}`)}
                                </span>
                            </div>
                        </div>

                        <div className="payment-total">
                            <span>{t('payment.amount')}</span>
                            <span className="amount">{booking.totalPrice.toFixed(2)} {booking.currency}</span>
                        </div>
                        
                        {/* Informacja o możliwości ponownej próby jeśli wcześniej była nieudana płatność */}
                        {booking.status === 'PAYMENT_FAILED' && (
                            <div className="payment-retry-info" style={{ marginTop: '1rem', padding: '0.75rem', backgroundColor: '#fff3cd', border: '1px solid #ffc107', borderRadius: '0.5rem', fontSize: '0.875rem', color: '#856404' }}>
                                Możesz ponownie spróbować zapłacić za tę rezerwację.
                            </div>
                        )}
                    </div>

                    <div className="payment-simulation card">
                        <h2 className="card-title">{t('payment.simulation')}</h2>
                        <div className="simulation-actions">
                            <button
                                onClick={() => handlePayment('SUCCESS')}
                                className="btn-primary btn-success"
                                disabled={loading}>
                                {loading ? t('common.processing') : t('payment.paySuccess')}
                            </button>

                            <button
                                onClick={() => handlePayment('FAIL')}
                                className="btn-danger"
                                disabled={loading}>
                                {loading ? t('common.processing') : t('payment.payFail')}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {error && (booking.status === 'PENDING_PAYMENT' || booking.status === 'PAYMENT_FAILED') && <div className="error">{error}</div>}

            {paymentResult && paymentResult.success && (
                <div className="payment-result-card card">
                    <div className="result-success">
                        <div className="result-icon">✓</div>
                        <h2>{t('payment.successTitle')}</h2>
                        <p>{t('payment.successMessage')}</p>

                        <div className="result-details">
                            <div className="detail">
                                <span>{t('payment.status')}</span>
                                <strong>{t(`payment.statusValue.${paymentResult.status}`)}</strong>
                            </div>
                            <div className="detail">
                                <span>{t('payment.reservationId')}</span>
                                <strong>#{bookingId}</strong>
                            </div>
                        </div>
                    </div>

                    <div className="result-actions">
                        <button onClick={() => navigate('/bookings')} className="btn-primary">
                            {t('payment.myBookingsButton')}
                        </button>
                        <button onClick={() => navigate('/')} className="btn-secondary">
                            {t('payment.searchNextButton')}
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default PaymentPage;
