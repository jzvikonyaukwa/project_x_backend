# axe_backend

## Machines:
1) Double roll-forming machine
   - ID 1
   - Sheet width: 925mm
   - Finishes: Galvanize, Chromadek
   - Gauges: 0.25, 0.27, 0.30, 0.40, 0.47, 0.50, 0.55, 0.58, 0.80
   - Profile: IBR, Corrugated
2) Roof Purlin and Ceiling Batten Machine', 
   - ID 2
   - Purlin  
     - Sheet Width: 150mm
     - Gauges: 0.80 (PREFERRED) || 0.55
   - Batten 
     - Sheet Width: 103mm
     - Gauges: 0.55 (PREFERRED) || 0.80
   - Finishes: Galvanize
   - Coating: Z150, Z275
   - Grade: ISQ550
3) Framecad machine ID 3
   - ID 3
   - Sheet width: 182mm
   - Finishes: Galvanize, Chromadek
   - Gauges: 0.80 (PREFERRED) || 1.00mm

# Conversions:

### Convert kgs to meters:

Meters = weight in kgs / conversionRate

```
if width = 925mm then we need the gauge to convert the weight to meters
    if gauge = 0.25 then conversionRate = 1.836
    if gauge = 0.27 then conversionRate = 1.983
    if gauge =0.30 then conversionRate = 2.204
    if gauge =0.40 then conversionRate = 2.974
    if gauge =0.47 then conversionRate = 3.494
    if gauge =0.50 then conversionRate = 3.718
    if gauge =0.55 then conversionRate = 4.0
    if gauge =0.58 then conversionRate = 4.312
    if gauge =0.80 then conversionRate = 5.949
else width != 925
    gauge * 1 * 8.039
```

## Quote's Status
- draft  
- accepted  
- rejected

## Cutting List's Status
- scheduled
- in-progress 
- canceled 
- completed

## Sheet's Status 
- scheduled 
- complete 
- canceled

## Coil's Status
- awaiting delivery 
- in stock 
- finished

# Outsourced Products

The following products are outsourced and can be added to quotes:

* ### Valley Gutters
* ### Roll Top Ridges
* ### Flashings

# Workflow

To add an outsourced product to a quote:

1. Select the product under the consumables section.
2. Specify the quantity and set the price per unit.

## Example
| Product | Quantity | Price per Unit |
|---------|----------|----------------|
| Roll Top Ridges | 8 | $2.10 |

## Quote Approval

When a quote containing outsourced products is accepted, a cutting list is automatically generated. 
This list calculates the necessary amount of raw material (plain sheet) for each outsourced product.

**Formula:**  
Total Length of Plain Sheet = Quantity × 2.4

## Delivery

Once the cutting list is completed, it will be ready for inclusion in the delivery note under the .