<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![](https://docs.simplicite.io//logos/logo250.png)
* * *

`Fixer` module definition
=========================

### Introduction

Fixer.io service conversion rates.

> **Note**: This module depends on ISO Currencies module.

### Import

To import this module:

- Create a module named `Fixer`
- Set the settings as:

```json
{
	"type": "git",
	"origin": {
		"uri": "https://github.com/simplicitesoftware/module-fixer.git"
	}
}
```

- Click on the _Import module_ button

### Configure

The configuration of the Fixer.io service is stored in the `FIXER_SETTINGS` system parameter.

### Refresh data

Use the _Refresh_ nutton on the rates business object.

`FixerRates` business object definition
---------------------------------------

Fixer currencies rates

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `fixerBaseCurId` link to **`ISOCurrency`**                   | id                                       | yes*     | yes       |          | Base currency                                                                    |
| _Ref. `fixerBaseCurId.isoCurCode`_                           | _char(3)_                                |          |           |          | _Currency code (3 letters)_                                                      |
| `fixerTargetCurId` link to **`ISOCurrency`**                 | id                                       | yes*     | yes       |          | Target currency                                                                  |
| _Ref. `fixerTargetCurId.isoCurCode`_                         | _char(3)_                                |          |           |          | _Currency code (3 letters)_                                                      |
| `fixerDate`                                                  | date                                     | yes*     | yes       |          | Date du taux de conversion                                                       |
| `fixerConversionRate`                                        | float(20, 6)                             | yes      | yes       |          | Conversion rate                                                                  |

### Custom actions

* `FixerRatesRefresh`: Refresh Fixer.io rates

