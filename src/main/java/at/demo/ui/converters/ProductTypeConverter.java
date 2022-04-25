package at.demo.ui.converters;

import at.demo.model.ProductType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@ApplicationScope
@Component
public class ProductTypeConverter extends EnumConverter<ProductType> {

    @Override
    public Class<ProductType> enumerationClass() {
        return ProductType.class;
    }
}