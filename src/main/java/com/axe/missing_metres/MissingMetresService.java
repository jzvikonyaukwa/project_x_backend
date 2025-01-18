package com.axe.missing_metres;


import com.axe.missing_metres.models.MissingMetersRequest;
import com.axe.productTransactions.ProductTransaction;
import com.axe.productTransactions.ProductTransactionsService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MissingMetresService {


    private final MissingMetresRepository missingMetresRepository;
    private final SteelCoilService steelCoilService;
    private final ProductTransactionsService productTransactionsService;

    public MissingMetresService(MissingMetresRepository missingMetresRepository, SteelCoilService steelCoilService,
                                ProductTransactionsService productTransactionsService) {
        this.missingMetresRepository = missingMetresRepository;
        this.steelCoilService = steelCoilService;
        this.productTransactionsService = productTransactionsService;
    }

    @Transactional
    public SteelCoil logMissingMeters(MissingMetersRequest missingMetersRequest) {
        if (missingMetersRequest == null) {
            throw new IllegalArgumentException("MissingMetresRequest cannot be null");
        }

        SteelCoil steelCoil = steelCoilService.getSteelCoil(missingMetersRequest.steelCoilId());


        MissingMetres missingMetres =  MissingMetres
                .builder()
                .mtrsMissing(missingMetersRequest.missingMeters())
                .reason(missingMetersRequest.reason())
                .status(Status.PENDING)
                .loggedAt(missingMetersRequest.loggedAt())
                .build();


        ProductTransaction productTransaction = productTransactionsService.createProductTransaction(
                steelCoil.getId(), missingMetersRequest.loggedAt(), missingMetres);
        missingMetres.setProductTransaction(productTransaction);



        missingMetresRepository.save(missingMetres);

        // Notify manager (email, SMS, etc.)
        return steelCoil;

    }

}
