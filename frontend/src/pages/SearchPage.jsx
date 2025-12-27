import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { flightApi } from '../api/apiClient';
import AirportSelector from '../components/AirportSelector';

function SearchPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        from: 'WAW',
        to: 'FCO',
        date: '2026-02-10',
        passengers: 1,
        travelClass: ''
    });
    const [flights, setFlights] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const response = await flightApi.searchFlights(
                formData.from,
                formData.to,
                formData.date,
                formData.passengers,
                formData.travelClass || undefined
            );
            setFlights(response.data);
        } catch (err) {
            setError(err.response?.data?.message || t('errors.search'));
        } finally {
            setLoading(false);
        }
    };

    const formatDateTime = (dateTime) => {
        return new Date(dateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    };

    const formatPrice = (price, currency) => {
        return `${price.toFixed(2)} ${currency}`;
    };

    const updatePassengers = (change) => {
        setFormData(prev => ({
            ...prev,
            passengers: Math.max(1, Math.min(10, prev.passengers + change))
        }));
    };

    return (
        <div>
            {/* Hero Section */}
            <div className="hero-wrapper">
                <h1 className="hero-title">{t('search.title')}</h1>
                <p className="hero-subtitle">{t('search.subtitle')}</p>
            </div>

            {/* Search Form */}
            <div className="search-card-container">
                <div className="search-card">
                    <form onSubmit={handleSubmit}>
                        <div className="search-grid">
                            <div className="form-group grid-from">
                                <AirportSelector
                                    label={t('search.from')}
                                    value={formData.from}
                                    onChange={(code) => setFormData({ ...formData, from: code })}
                                    placeholder={t('search.fromPlaceholder')}
                                    icon="🛫"
                                />
                            </div>

                            <div className="form-group grid-to">
                                <AirportSelector
                                    label={t('search.to')}
                                    value={formData.to}
                                    onChange={(code) => setFormData({ ...formData, to: code })}
                                    placeholder={t('search.toPlaceholder')}
                                    icon="🛬"
                                />
                            </div>

                            <div className="form-group grid-date">
                                <label>{t('search.date')}</label>
                                <input
                                    type="date"
                                    className="search-input"
                                    name="date"
                                    value={formData.date}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="form-group grid-passengers">
                                <label>{t('search.passengers')}</label>
                                <div className="passengers-selector">
                                    <button type="button" onClick={() => updatePassengers(-1)} disabled={formData.passengers <= 1}>−</button>
                                    <span>{formData.passengers}</span>
                                    <button type="button" onClick={() => updatePassengers(1)} disabled={formData.passengers >= 10}>+</button>
                                </div>
                            </div>

                            <div className="form-group grid-class">
                                <label>{t('flightDetails.class')}</label>
                                <select
                                    className="search-input"
                                    name="travelClass"
                                    value={formData.travelClass}
                                    onChange={handleChange}
                                >
                                    <option value="">{t('search.anyClass')}</option>
                                    <option value="ECONOMY">{t('flightDetails.economy')}</option>
                                    <option value="BUSINESS">{t('flightDetails.business')}</option>
                                    <option value="FIRST">{t('flightDetails.first')}</option>
                                </select>
                            </div>
                        </div>

                        <div className="form-actions">
                            <button type="submit" className="btn-search" disabled={loading}>
                                {loading ? (
                                    <span>{t('search.searching')}</span>
                                ) : (
                                    <>
                                        <span>{t('search.searchButton')}</span>
                                    </>
                                )}
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            {/* Results Section */}
            <div className="results-container">
                {error && <div className="error">{error}</div>}

                {flights.length > 0 && (
                    <>
                        <h2 className="section-title">{t('search.results')} ({flights.length})</h2>
                        {flights.map((flight) => (
                            <div key={flight.id} className="flight-card" onClick={() => navigate(`/flights/${flight.id}?passengers=${formData.passengers}`)}>
                                <div className="flight-route-info">
                                    <div className="flight-time">{formatDateTime(flight.departureAt)}</div>
                                    <div className="flight-airport">{flight.fromAirport}</div>
                                </div>

                                <div className="flight-duration">
                                    <span>{flight.durationMinutes} min</span>
                                    <div className="flight-duration-line"></div>
                                    <span>{t('search.nonStop')}</span>
                                </div>

                                <div className="flight-route-info">
                                    <div className="flight-time">{formatDateTime(flight.arrivalAt)}</div>
                                    <div className="flight-airport">{flight.toAirport}</div>
                                </div>

                                <div className="flight-price-section">
                                    <span className="flight-price">{formatPrice(flight.price, flight.currency)}</span>
                                    <span className="flight-class-badge">{flight.travelClass}</span>
                                    <div style={{ marginTop: '0.5rem', fontSize: '0.8rem', color: '#666' }}>
                                        {flight.availableSeats} {t('flightDetails.seats')}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </>
                )}
            </div>
        </div>
    );
}

export default SearchPage;
