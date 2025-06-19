/*
 * Copyright (C) 2025 Rostislav Suleimanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

<?php

class ArrayOperationsTest
{
    public function nonCompliantArrayOperations(): void
    {
        $result = [];
        $items = $this->getItems();


        foreach ($items as $item) {
            $result = array_merge($result, $item->getValues()); // Noncompliant {{array_merge() is used in a loop and is a resource-intensive operation.}}
        }

        $datasets = $this->getDatasets();
        $combinedData = [];


        for ($i = 0; $i < count($datasets); $i++) {
            $combinedData = array_merge_recursive($combinedData, $datasets[$i]); // Noncompliant {{array_merge_recursive() is used in a loop and is a resource-intensive operation.}}
        }

        $counter = 0;
        $masterArray = ['a' => 1, 'b' => 2, 'c' => 3];
        $differences = [];


        while ($counter < 10) {
            $testArray = $this->getRandomArray();
            $differences[] = array_diff($masterArray, $testArray); // Noncompliant {{array_diff() is used in a loop and is a resource-intensive operation.}}
            $counter++;
        }

        $index = 0;
        $comparisonArrays = $this->getComparisonArrays();

        do {
            $commonElements = array_intersect($masterArray, $comparisonArrays[$index]);  // Noncompliant {{array_intersect() is used in a loop and is a resource-intensive operation.}}
            $this->processCommonElements($commonElements);
            $index++;
        } while ($index < count($comparisonArrays));


        foreach ($this->getConfigurations() as $config) {
            $defaultConfig = $this->getDefaultConfig();
            $activeConfig = array_replace($defaultConfig, $config); // Noncompliant {{array_replace() is used in a loop and is a resource-intensive operation.}}
            $this->applyConfiguration($activeConfig);
        }
    }

    public function compliantArrayOperations(): void
    {
        $collections = [];
        $items = $this->getItems();
        foreach ($items as $item) {
            $collections[] = $item->getValues();
        }
        $result = array_merge(...$collections); // Compliant: Collect all items first, then merge once outside the loop

        $datasets = $this->getDatasets();
        $combinedData = [];
        for ($i = 0; $i < count($datasets); $i++) {
            foreach ($datasets[$i] as $key => $value) {
                $combinedData[$key] = $value; // Compliant: Direct array addition within the loop
            }
        }


        $configs = $this->getConfigurations();
        $defaultConfig = $this->getDefaultConfig();
        foreach ($configs as $config) {
            $activeConfig = $defaultConfig + $config; // Compliant: Using array union operator instead of array_merge
            $this->applyConfiguration($activeConfig);
        }


        $largeDataset = $this->getLargeDataset();
        $chunkSize = 100;
        $chunks = array_chunk($largeDataset, $chunkSize);

        foreach ($chunks as $chunk) {
            $processedChunk = $this->processDataChunk($chunk); // Compliant: Process in chunks to avoid loop with expensive operations
            $this->saveResults($processedChunk);
        }
    }

    public function mixedArrayOperationsExample(): void
    {
        $items = $this->getItems();

        $result1 = [];

        foreach ($items as $item) {
            if ($item->isSpecial()) {
                $result1 = array_merge($result1, $item->getSpecialValues()); // Noncompliant {{array_merge() is used in a loop and is a resource-intensive operation.}}
            }
        }

        $result2 = [];
        $specialValues = [];
        foreach ($items as $item) {
            if ($item->isImportant()) {
                $specialValues[] = $item->getImportantValues();
            }
        }
        if (!empty($specialValues)) {
            $result2 = array_merge(...$specialValues); //Compliant: Single merge operation outside the loop
        }


        $masterConfig = $this->getMasterConfig();
        foreach ($this->getUserConfigs() as $userConfig) {
            $differences = array_diff_assoc($masterConfig, $userConfig); // Noncompliant {{array_diff_assoc() is used in a loop and is a resource-intensive operation.}}
            $this->reportConfigDifferences($differences);
        }
    }

    private function getItems(): array { return []; }
    private function getDatasets(): array { return []; }
    private function getRandomArray(): array { return []; }
    private function getComparisonArrays(): array { return []; }
    private function getConfigurations(): array { return []; }
    private function getDefaultConfig(): array { return []; }
    private function getLargeDataset(): array { return []; }
    private function getMasterConfig(): array { return []; }
    private function getUserConfigs(): array { return []; }

    private function processCommonElements(array $elements): void {}
    private function applyConfiguration(array $config): void {}
    private function processDataChunk(array $chunk): array { return []; }
    private function saveResults(array $results): void {}
    private function reportConfigDifferences(array $diff): void {}
}