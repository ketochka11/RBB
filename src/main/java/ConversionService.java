public interface ConversionService {
    static ConversionService getInstance(){
        return (ConversionService) new NbrbConversionService();
    }
    double getConversion(Currency original, Currency target);
}
