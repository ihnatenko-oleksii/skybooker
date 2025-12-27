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
            setPaymentResult({
                success: outcome === 'SUCCESS',
                status: response.data.status,
                bookingStatus: response.data.bookingStatus
            });
        } catch (err) {
            setError(err.response?.data?.message || t('errors.processPayment'));
        } finally {
            setLoading(false);
        }
    };

    if (error && !booking) return <div className="container"><div className="error">{error}</div></div>;
    if (!booking) return <div className="loading">{t('common.loading')}</div>;

    return (
        <div className="payment-container">
            <h1 className="section-title">{t('payment.title')}</h1>

            {!paymentResult && (
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

            {error && <div className="error">{error}</div>}

            {paymentResult && (
                <div className="payment-result-card card">
                    {paymentResult.success ? (
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
                    ) : (
                        <div className="result-fail">
                            <div className="result-icon">✗</div>
                            <h2>{t('payment.failTitle')}</h2>
                            <p>{t('payment.failMessage')}</p>
                        </div>
                    )}

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
